package com.cencen.storyu

import com.cencen.storyu.data.calls.SigninRequestCall
import com.cencen.storyu.data.calls.SignupRequestCall
import com.cencen.storyu.data.connection.api.ApiServices
import com.cencen.storyu.data.connection.resp.MainResponse
import com.cencen.storyu.data.connection.resp.SigninResponse
import com.cencen.storyu.data.connection.resp.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiServices : ApiServices {

    private val dumSigninResponse = DataDummies.generatesDummySigninResponse()
    private val dumSignupResponse = DataDummies.generatesDummySignupResponse()
    private val dumStoriesResponse = DataDummies.generatesDummyStoryLoc()
    private val dumUploadStoryResponse = DataDummies.generatesDummyUploadStoryResponse()

    override suspend fun signup(request: SignupRequestCall): MainResponse {
        return dumSignupResponse
    }

    override suspend fun signin(request: SigninRequestCall): SigninResponse {
        return dumSigninResponse
    }

    override suspend fun getMemberStory(token: String, page: Int, size: Int): StoriesResponse {
        return dumStoriesResponse
    }

    override suspend fun getMemberStoryLoc(token: String, location: Int): StoriesResponse {
        return dumStoriesResponse
    }

    override suspend fun addMemberStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): MainResponse {
        return dumUploadStoryResponse
    }

}