package com.cencen.storyu.view.storymaps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cencen.storyu.data.models.Member
import com.cencen.storyu.data.repositories.StoryRepositories

class MapsViewModel(private val rep: StoryRepositories) : ViewModel() {
    fun getMemberStoryLoc(tokens: String) = rep.getMemberStoryLoc(tokens)
    fun getMember(): LiveData<Member> {
        return rep.getMemberData()
    }
}