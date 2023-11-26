package com.cencen.storyu.view.storymaps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.cencen.storyu.DataDummies
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.data.connection.resp.StoriesResponse
import com.cencen.storyu.data.repositories.StoryRepositories
import com.cencen.storyu.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    val executorInstant = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepositories: StoryRepositories
    private lateinit var mapsViewModel: MapsViewModel
    private val dumStoryWithLocation = DataDummies.generatesDummyStoryLoc()
    private val tokens = "TOKEN"

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(storyRepositories)
    }

    @Test
    fun `when get Story with Location Successful`() {
        val expectStory = MutableLiveData<Libraries<StoriesResponse>>()
        expectStory.value = Libraries.Success(dumStoryWithLocation)
        `when`(storyRepositories.getMemberStoryLoc(tokens)).thenReturn(expectStory)
        val recentStory = mapsViewModel.getMemberStoryLoc(tokens).getOrAwaitValue()
        Mockito.verify(storyRepositories).getMemberStoryLoc(tokens)
        assertNotNull(recentStory)
        assertTrue(recentStory is Libraries.Success)
    }
}