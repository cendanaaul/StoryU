package com.cencen.storyu.view.adaptor

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.databinding.ItemStorycardBinding
import com.cencen.storyu.view.story.InformationStoryActivity

class StoryAdaptor : PagingDataAdapter<RosterStory, StoryAdaptor.StoryViewHolder>(DIFF_CALL) {

    companion object {
        val DIFF_CALL = object : DiffUtil.ItemCallback<RosterStory>() {
            override fun areItemsTheSame(oldItem: RosterStory, newItem: RosterStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RosterStory, newItem: RosterStory): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val ui = ItemStorycardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(ui)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, loc: Int) {
        val item = getItem(loc)
        if (item != null) holder.bind(item)
    }

    class StoryViewHolder(private val binding: ItemStorycardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(memberStory: RosterStory) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(memberStory.photoUrl)
                    .into(binding.ivStorymembertake)
                tvNamemember.text = memberStory.name
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, InformationStoryActivity::class.java).apply {
                    putExtra(InformationStoryActivity.EXTRA_DETAIL, memberStory)
                }
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivStorymembertake, "image"),
                    Pair(binding.tvNamemember, "name")
                )
                it.context.startActivity(intent, options.toBundle())
            }
        }
    }
}
