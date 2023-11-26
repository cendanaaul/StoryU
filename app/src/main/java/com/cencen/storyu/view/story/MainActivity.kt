package com.cencen.storyu.view.story

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cencen.storyu.databinding.ActivityMainBinding
import com.cencen.storyu.utility.ViewModelProviderFactory
import com.cencen.storyu.view.adaptor.LoadStateAdaptor
import com.cencen.storyu.view.adaptor.StoryAdaptor
import com.cencen.storyu.view.member.MemberViewModel
import com.cencen.storyu.view.member.SigninActivity
import com.cencen.storyu.view.storymaps.MapsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storiesAdaptor: StoryAdaptor
    private lateinit var mainViewModel: MainViewModel
    private lateinit var memberViewModel: MemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showProcessing(true)
        supportActionBar?.hide()

        initializeVM()
        initializeView()
        onClickFAB()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    private fun onClickFAB() {
        val menuFab = binding.fabMenustory
        binding.fabSignoutmember.setOnClickListener {
            memberViewModel.signout()
            startActivity(Intent(this, SigninActivity::class.java))
            finishAffinity()
        }
        binding.fabUploadstorymember.setOnClickListener {
            startActivity(Intent(this, UploadStoryActivity::class.java))
            menuFab.close(false)
        }
        binding.fabSettingmember.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            menuFab.close(false)
        }
        binding.fabMapstory.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
            menuFab.close(false)
        }
    }

    private fun initializeView() {
        storiesAdaptor = StoryAdaptor()

        mainViewModel.getMember().observe(this@MainActivity) { member ->
            if (member.isSignin) {
                initializeMemberStory()
            } else {
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            }
        }

        with(binding.rvStorymember) {
            setHasFixedSize(true)
            adapter = storiesAdaptor.withLoadStateFooter(
                footer = LoadStateAdaptor {
                    storiesAdaptor.retry()
                }
            )
        }
    }

    private fun initializeVM() {
        val fact: ViewModelProviderFactory = ViewModelProviderFactory.getInstance(this)

        mainViewModel = ViewModelProvider(this, fact)[MainViewModel::class.java]
        memberViewModel = ViewModelProvider(this, fact)[MemberViewModel::class.java]

    }

    private fun showProcessing(isLoad: Boolean) {
        binding.loadingProcess.visibility = if (isLoad) View.VISIBLE else View.GONE
    }

    private fun initializeMemberStory() {
        mainViewModel.getMemberStory().observe(this@MainActivity) {
            storiesAdaptor.submitData(lifecycle, it)
            showProcessing(false)
        }
    }
}