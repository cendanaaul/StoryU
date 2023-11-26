package com.cencen.storyu.view.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.cencen.storyu.DataDummies
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.data.connection.resp.MainResponse
import com.cencen.storyu.data.repositories.StoryRepositories
import com.cencen.storyu.getOrAwaitValue
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class UploadStoryViewModelTest {
    @get:Rule
    var executorInstant = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepositories: StoryRepositories
    private lateinit var uploadStoryViewModel: UploadStoryViewModel
    private val dumUploadStory = DataDummies.generatesDummyUploadStoryResponse()
    private val tokens = "TOKEN"

    @Before
    fun setup() {
        uploadStoryViewModel = UploadStoryViewModel(storyRepositories)
    }

    @Test
    fun `when Upload Story Successful`() {
        val desc = "description".toRequestBody("text/plain".toMediaType())
        val files = Mockito.mock(File::class.java)
        val reqImgFile = files.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            files.name,
            reqImgFile
        )

        val expectStory = MutableLiveData<Libraries<MainResponse>>()
        expectStory.value = Libraries.Success(dumUploadStory)
        `when`(storyRepositories.addMemberStory(tokens, imgMultipart, desc)).thenReturn(expectStory)

        val recentStory =
            uploadStoryViewModel.addMemberStory(tokens, imgMultipart, desc).getOrAwaitValue()

        Mockito.verify(storyRepositories).addMemberStory(tokens, imgMultipart, desc)
        assertNotNull(recentStory)
        assertTrue(recentStory is Libraries.Success)
    }
}