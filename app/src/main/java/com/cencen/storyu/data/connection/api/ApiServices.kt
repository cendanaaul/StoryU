package com.cencen.storyu.data.connection.api

import com.cencen.storyu.data.calls.SigninRequestCall
import com.cencen.storyu.data.calls.SignupRequestCall
import com.cencen.storyu.data.connection.resp.MainResponse
import com.cencen.storyu.data.connection.resp.SigninResponse
import com.cencen.storyu.data.connection.resp.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiServices {
    @POST("register")
    suspend fun signup(
        @Body request: SignupRequestCall
    ): MainResponse

    @POST("login")
    suspend fun signin(
        @Body request: SigninRequestCall
    ): SigninResponse

    @GET("stories")
    suspend fun getMemberStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

    @GET("stories")
    suspend fun getMemberStoryLoc(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addMemberStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): MainResponse


}