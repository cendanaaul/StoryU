package com.cencen.storyu.view.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cencen.storyu.data.models.Member
import com.cencen.storyu.data.repositories.StoryRepositories
import kotlinx.coroutines.launch

class MemberViewModel(private val rep: StoryRepositories) : ViewModel() {
    fun memberSignin(email: String, pass: String) = rep.memberSignin(email, pass)
    fun memberSignup(name: String, email: String, pass: String) =
        rep.memberSignup(name, email, pass)

    fun saveMember(mem: Member) {
        viewModelScope.launch { rep.saveMemberData(mem) }
    }

    fun signin() {
        viewModelScope.launch { rep.signin() }
    }

    fun signout() {
        viewModelScope.launch { rep.signout() }
    }
}