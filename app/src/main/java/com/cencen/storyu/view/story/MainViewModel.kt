package com.cencen.storyu.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cencen.storyu.data.models.Member
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.data.repositories.StoryRepositories

class MainViewModel(private val rep: StoryRepositories) : ViewModel() {
    fun getMemberStory(): LiveData<PagingData<RosterStory>> {
        return rep.getMemberStory().cachedIn(viewModelScope)
    }

    fun getMember(): LiveData<Member> {
        return rep.getMemberData()
    }
}