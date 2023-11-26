package com.cencen.storyu.view.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.cencen.storyu.DataDummies
import com.cencen.storyu.MainDispatcherRule
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.data.repositories.StoryRepositories
import com.cencen.storyu.getOrAwaitValue
import com.cencen.storyu.view.adaptor.StoryAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val executorInstant = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepositories: StoryRepositories

    @Test
    fun `when get Story Member successful`() = runTest {
        val dumStory = DataDummies.generatesDummyStory()
        val data: PagingData<RosterStory> = StoryPageSource.snapshot(dumStory)
        val expectStory = MutableLiveData<PagingData<RosterStory>>()

        expectStory.value = data
        `when`(storyRepositories.getMemberStory()).thenReturn(expectStory)

        val mainVM = MainViewModel(storyRepositories)
        val recentStory: PagingData<RosterStory> = mainVM.getMemberStory().getOrAwaitValue()

        val diffing = AsyncPagingDataDiffer(
            diffCallback = StoryAdaptor.DIFF_CALL,
            updateCallback = noopListUpdateCalls,
            workerDispatcher = Dispatchers.Main,
        )
        diffing.submitData(recentStory)

        val diffSnapshots = diffing.snapshot()

        assertNotNull(diffSnapshots)
        assertEquals(10, diffSnapshots.size)

        val firstData = dumStory[0]
        assertEquals(firstData, diffSnapshots[0])
    }

    @Test
    fun `when There is No Story`() = runTest {
        val emptyData: PagingData<RosterStory> = StoryPageSource.snapshot(emptyList())
        val expectStory = MutableLiveData<PagingData<RosterStory>>()

        expectStory.value = emptyData
        `when`(storyRepositories.getMemberStory()).thenReturn(expectStory)

        val mainVM = MainViewModel(storyRepositories)
        val recentStory: PagingData<RosterStory> = mainVM.getMemberStory().getOrAwaitValue()

        val diffing = AsyncPagingDataDiffer(
            diffCallback = StoryAdaptor.DIFF_CALL,
            updateCallback = noopListUpdateCalls,
            workerDispatcher = Dispatchers.Main,
        )
        diffing.submitData(recentStory)

        val emptyDiffSnapshots = diffing.snapshot()

        assertEquals(0, emptyDiffSnapshots.size)
    }
}


class StoryPageSource : PagingSource<Int, LiveData<List<RosterStory>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<RosterStory>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<RosterStory>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(list: List<RosterStory>): PagingData<RosterStory> {
            return PagingData.from(list)
        }
    }

}


val noopListUpdateCalls = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
