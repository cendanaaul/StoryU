package com.cencen.storyu.view.story

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.databinding.ActivityInformationStoryBinding

class InformationStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformationStoryBinding

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showProcessing(true)
        initializeView()
    }

    private fun initializeView() {
        val info = intent.getParcelableExtra<RosterStory>(EXTRA_DETAIL)

        binding.apply {
            tvNameMember.text = info?.name
            tvDescinfo.text = info?.description
        }
        Glide.with(this)
            .load(info?.photoUrl)
            .into(binding.ivStoryinformation)
        showProcessing(false)
    }

    private fun showProcessing(isLoad: Boolean) {
        binding.loadingProcess.visibility = if (isLoad) View.VISIBLE else View.GONE
    }
}