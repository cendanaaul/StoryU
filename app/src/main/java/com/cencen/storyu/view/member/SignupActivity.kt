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
import com.cencen.storyu.databinding.ActivitySignupBinding
import com.cencen.storyu.utility.ViewModelProviderFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var memberViewModel: MemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initializeViewModel()
        initializeAction()
        initializeAnimation()
    }

    private fun initializeAction() {
        binding.tvGotosignin.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }
        binding.btnSignUp.setOnClickListener {
            if (isRightInput()) {
                val name = binding.etNamemembers.text.toString()
                val mail = binding.etEmailmembers.text.toString()
                val pass = binding.etPassmembers.text.toString()
                memberViewModel.memberSignup(name, mail, pass).observe(this) {
                    when (it) {
                        is Libraries.Success -> {
                            showProcessing(false)
                            showToast(it.data.toString())
                            startActivity(Intent(this, SigninActivity::class.java))
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
    }

    private fun isRightInput() =
        binding.etEmailmembers.error == null &&
                binding.etPassmembers.error == null &&
                binding.etNamemembers.error == null &&
                !binding.etEmailmembers.text.isNullOrEmpty() &&
                !binding.etPassmembers.text.isNullOrEmpty() &&
                !binding.etNamemembers.text.isNullOrEmpty()

    private fun initializeAnimation() {
        val viewsToAnimate = listOf(
            binding.ivSignupmember, binding.etNamemembers, binding.etEmailmembers,
            binding.etPassmembers, binding.btnSignUp, binding.tvHaveacc, binding.tvGotosignin
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

    private fun initializeViewModel() {
        val fact: ViewModelProviderFactory = ViewModelProviderFactory.getInstance(this)
        memberViewModel = ViewModelProvider(this, fact)[MemberViewModel::class.java]
    }

    private fun showProcessing(load: Boolean) {
        binding.loadingbars.visibility = if (load) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun goToSignin() {
        val intent = Intent(this, SigninActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
