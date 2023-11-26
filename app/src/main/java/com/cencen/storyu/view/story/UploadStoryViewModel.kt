package com.cencen.storyu.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cencen.storyu.data.models.Member
import com.cencen.storyu.data.repositories.StoryRepositories
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val rep: StoryRepositories) : ViewModel() {
    fun addMemberStory(tokens: String, files: MultipartBody.Part, des: RequestBody) =
        rep.addMemberStory(tokens, files, des)

    fun getMember(): LiveData<Member> {
        return rep.getMemberData()
    }
}