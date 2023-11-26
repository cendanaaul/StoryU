package com.cencen.storyu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.cencen.storyu.DataDummies
import com.cencen.storyu.FakeApiServices
import com.cencen.storyu.MainDispatcherRule
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.data.connection.api.ApiServices
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.data.settingpreference.PreferencesSetting
import com.cencen.storyu.getOrAwaitValue
import com.cencen.storyu.observeForTesting
import com.cencen.storyu.view.adaptor.StoryAdaptor
import com.cencen.storyu.view.story.StoryPageSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoriesTest {
    @get:Rule
    var executorInstant = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    @Mock
    private lateinit var apis: ApiServices

    @Mock
    private lateinit var preference: PreferencesSetting

    @Mock
    private lateinit var storyRepositories: StoryRepositories

    @Before
    fun setup() {
        apis = FakeApiServices()
        storyRepositories = StoryRepositories(preference, apis)
    }

    @Test
    fun `when Member Signup Successful`() = runTest {
        val expectMember = DataDummies.generatesDummySignupResponse()
        val recentMember = storyRepositories.memberSignup(name, email, password)
        recentMember.observeForTesting {
            assertNotNull(recentMember)
            assertEquals(
                expectMember.message,
                (recentMember.value as Libraries.Success).data.message
            )
        }
    }

    @Test
    fun `when Member Signin Successful`() = runTest {
        val expectMember = DataDummies.generatesDummySigninResponse()
        val recentMember = storyRepositories.memberSignin(email, password)
        recentMember.observeForTesting {
            assertNotNull(recentMember)
            assertEquals(
                expectMember.message,
                (recentMember.value as Libraries.Success).data.message
            )
        }
    }

    @Test
    fun `when get Story Member successful`() = runTest {
        val classMocked = mock(StoryRepositories::class.java)
        val expectStory = MutableLiveData<PagingData<RosterStory>>()
        expectStory.value = StoryPageSource.snapshot(DataDummies.generatesDummyStory())
        `when`(classMocked.getMemberStory()).thenReturn(expectStory)
        val recentStory = classMocked.getMemberStory().getOrAwaitValue()
        val diffing = AsyncPagingDataDiffer(
            diffCallback = StoryAdaptor.DIFF_CALL,
            updateCallback = object : ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {}
                override fun onMoved(fromPosition: Int, toPosition: Int) {}
                override fun onChanged(position: Int, count: Int, payload: Any?) {}
                override fun onRemoved(position: Int, count: Int) {}
            },
            workerDispatcher = Dispatchers.Main,
        )
        diffing.submitData(recentStory)
        verify(classMocked).getMemberStory()
        assertNotNull(recentStory)
        assertEquals(DataDummies.generatesDummyStory(), diffing.snapshot())
        assertEquals(DataDummies.generatesDummyStory().size, diffing.snapshot().size)
    }

    @Test
    fun `when Upload Story Successful`() = runTest {
        val desc = "description".toRequestBody("text/plain".toMediaType())
        val files = mock(File::class.java)
        val reqImgFile = files.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            files.name,
            reqImgFile
        )
        val expectStory = DataDummies.generatesDummyUploadStoryResponse()
        val recentStory = storyRepositories.addMemberStory(tokens, imgMultipart, desc)
        recentStory.observeForTesting {
            assertNotNull(recentStory)
            assertEquals(expectStory.message, (recentStory.value as Libraries.Success).data.message)
        }
    }

    companion object {
        private const val name = "name"
        private const val email = "mail@yahoo.com"
        private const val password = "password"
        private const val tokens = "TOKEN"
    }
}