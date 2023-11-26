package com.cencen.storyu.view.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cencen.storyu.databinding.ItemReloadBinding

class LoadStateAdaptor(private val reload: () -> Unit) :
    LoadStateAdapter<LoadStateAdaptor.LoadStateViewHolder>() {

    class LoadStateViewHolder(private val binding: ItemReloadBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnReload.setOnClickListener { retry.invoke() }
        }

        fun bind(stateReload: LoadState) {
            if (stateReload is LoadState.Error) {
                binding.tvError.text = stateReload.error.localizedMessage
            }
            binding.pbError.isVisible = stateReload is LoadState.Loading
            binding.btnReload.isVisible = stateReload is LoadState.Error
            binding.tvError.isVisible = stateReload is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemReloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, reload)
    }
}