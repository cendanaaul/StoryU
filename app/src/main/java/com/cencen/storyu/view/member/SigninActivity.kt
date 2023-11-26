package com.cencen.storyu.view.member

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cencen.storyu.R
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.data.models.Member
import com.cencen.storyu.databinding.ActivitySigninBinding
import com.cencen.storyu.utility.ViewModelProviderFactory
import com.cencen.storyu.view.story.MainActivity

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var memberViewModel: MemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initializeVM()
        initializeAction()
        initializeAnimation()
    }

    private fun initializeAction() {
        binding.btnSignIn.setOnClickListener {
            if (isRightInput()) {
                val mail = binding.etEmailmember.text.toString()
                val pass = binding.etPasswember.text.toString()
                memberViewModel.memberSignin(mail, pass).observe(this) {
                    when (it) {
                        is Libraries.Success -> {
                            showProcessing(false)
                            val responses = it.data
                            saveMemberData(
                                Member(
                                    responses.loginResult?.name.toString(),
                                    responses.loginResult?.token.toString(),
                                    true
                                )
                            )
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        }

                        is Libraries.Loading -> showProcessing(true)
                        is Libraries.Error -> {
                            showToast(it.error)
                            showProcessing(false)
                        }
                    }
                }
            } else {
                showToast(getString(R.string.wrong_input))
            }
        }

        binding.tvGotosignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initializeAnimation() {
        val viewsToAnimate = listOf(
            binding.ivSignIn,
            binding.etEmailmember,
            binding.etPasswember,
            binding.btnSignIn,
            binding.tvNotmember,
            binding.tvGotosignup
        )

        val animators = mutableListOf<Animator>()
        viewsToAnimate.forEach { view ->
            val animator = ObjectAnimator.ofFloat(view, View.ROTATION, -90f, 0f)
            animator.duration = 700
            animators.add(animator)
        }

        AnimatorSet().apply {
            playTogether(animators)
            start()
        }

    }

    private fun initializeVM() {
        val fact: ViewModelProviderFactory = ViewModelProviderFactory.getInstance(this)
        memberViewModel = ViewModelProvider(this, fact)[MemberViewModel::class.java]
    }

    private fun showProcessing(load: Boolean) {
        binding.loadingbars.visibility = if (load) View.VISIBLE else View.GONE
    }

    private fun isRightInput(): Boolean {
        return binding.etEmailmember.error == null &&
                binding.etPasswember.error == null &&
                !binding.etEmailmember.text.isNullOrEmpty() &&
                !binding.etPasswember.text.isNullOrEmpty()
    }

    private fun saveMemberData(mem: Member) {
        memberViewModel.saveMember(mem)
    }
}