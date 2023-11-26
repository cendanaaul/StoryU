package com.cencen.storyu.utility

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cencen.storyu.data.repositories.StoryRepositories
import com.cencen.storyu.di.InjectionStory
import com.cencen.storyu.view.member.MemberViewModel
import com.cencen.storyu.view.story.MainViewModel
import com.cencen.storyu.view.story.UploadStoryViewModel
import com.cencen.storyu.view.storymaps.MapsViewModel

class ViewModelProviderFactory(private val rep: StoryRepositories) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemberViewModel::class.java)) {
            return MemberViewModel(rep) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(rep) as T
        }
        if (modelClass.isAssignableFrom(UploadStoryViewModel::class.java)) {
            return UploadStoryViewModel(rep) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(rep) as T
        }

        throw IllegalArgumentException("Undetected vm : " + modelClass.simpleName)
    }

    companion object {
        @Volatile
        private var instance: ViewModelProviderFactory? = null
        fun getInstance(context: Context): ViewModelProviderFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelProviderFactory(InjectionStory.setRepositories(context))
            }.also { instance = it }
        }
    }
}