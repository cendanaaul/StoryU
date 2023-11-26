package com.cencen.storyu.view.member

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.cencen.storyu.DataDummies
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.data.connection.resp.MainResponse
import com.cencen.storyu.data.connection.resp.SigninResponse
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
class MemberViewModelTest {
    @get:Rule
    var executorInstant = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepositories: StoryRepositories
    private lateinit var memberViewModel: MemberViewModel
    private val dumSignin = DataDummies.generatesDummySigninResponse()
    private val dumSignup = DataDummies.generatesDummySignupResponse()
    private val name = "name"
    private val email = "mail@yahoo.com"
    private val password = "password"

    @Before
    fun setup() {
        memberViewModel = MemberViewModel(storyRepositories)
    }

    @Test
    fun `when Member Signin Successful`() {
        val expectMember = MutableLiveData<Libraries<SigninResponse>>()
        expectMember.value = Libraries.Success(dumSignin)
        `when`(storyRepositories.memberSignin(email, password)).thenReturn(expectMember)
        val recentMember = memberViewModel.memberSignin(email, password).getOrAwaitValue()
        Mockito.verify(storyRepositories).memberSignin(email, password)
        assertNotNull(recentMember)
        assertTrue(recentMember is Libraries.Success)
    }

    @Test
    fun `when Member Signup Successful`() {
        val expectMember = MutableLiveData<Libraries<MainResponse>>()
        expectMember.value = Libraries.Success(dumSignup)
        `when`(storyRepositories.memberSignup(name, email, password)).thenReturn(expectMember)
        val recentMember = memberViewModel.memberSignup(name, email, password).getOrAwaitValue()
        Mockito.verify(storyRepositories).memberSignup(name, email, password)
        assertNotNull(recentMember)
        assertTrue(recentMember is Libraries.Success)
    }
}