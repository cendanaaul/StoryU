package com.cencen.storyu.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.data.StoryPageSources
import com.cencen.storyu.data.calls.SigninRequestCall
import com.cencen.storyu.data.calls.SignupRequestCall
import com.cencen.storyu.data.connection.api.ApiServices
import com.cencen.storyu.data.connection.resp.MainResponse
import com.cencen.storyu.data.connection.resp.SigninResponse
import com.cencen.storyu.data.connection.resp.StoriesResponse
import com.cencen.storyu.data.models.Member
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.data.settingpreference.PreferencesSetting
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepositories(private val conf: PreferencesSetting, private val apis: ApiServices) {

    suspend fun signin() {
        conf.signin()
    }

    fun memberSignin(mail: String, pass: String): LiveData<Libraries<SigninResponse>> = liveData {
        emit(Libraries.Loading)
        try {
            val resp = apis.signin(SigninRequestCall(mail, pass))
            emit(Libraries.Success(resp))
        } catch (ex: Exception) {
            Log.d("Signin ", ex.message.toString())
            emit(Libraries.Error(ex.message.toString()))
        }
    }

    fun memberSignup(name: String, email: String, pass: String): LiveData<Libraries<MainResponse>> =
        liveData {
            emit(Libraries.Loading)
            try {
                val res = apis.signup(
                    SignupRequestCall(name, email, pass)
                )
                emit(Libraries.Success(res))
            } catch (ex: Exception) {
                Log.d("Signup ", ex.message.toString())
                emit(Libraries.Error(ex.message.toString()))
            }
        }

    suspend fun saveMemberData(mem: Member) {
        conf.saveMemberData(mem)
    }

    fun getMemberData(): LiveData<Member> {
        return conf.getMemberData().asLiveData()
    }

    //
    fun getMemberStoryLoc(tokens: String): LiveData<Libraries<StoriesResponse>> =
        liveData {
            emit(Libraries.Loading)
            try {
                val resp = apis.getMemberStoryLoc(tokens, 1)
                emit(Libraries.Success(resp))
            } catch (ex: Exception) {
                Log.d("Signup ", ex.message.toString())
                emit(Libraries.Error(ex.message.toString()))
            }
        }

    fun getMemberStory(): LiveData<PagingData<RosterStory>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPageSources(apis, conf)
            }
        ).liveData
    }

    fun addMemberStory(
        tokens: String,
        files: MultipartBody.Part,
        desc: RequestBody
    ): LiveData<Libraries<MainResponse>> = liveData {
        emit(Libraries.Loading)
        try {
            val responses = apis.addMemberStory(tokens, files, desc)
            emit(Libraries.Success(responses))
        } catch (ex: Exception) {
            Log.d("Signup ", ex.message.toString())
            emit(Libraries.Error(ex.message.toString()))
        }
    }

    suspend fun signout() {
        conf.signout()
    }


    companion object {
        @Volatile
        private var instance: StoryRepositories? = null
        fun getInstance(
            conf: PreferencesSetting,
            apis: ApiServices
        ): StoryRepositories =
            instance ?: synchronized(this) {
                instance ?: StoryRepositories(conf, apis)
            }.also { instance = it }
    }
}