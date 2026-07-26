package com.example.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.home.databinding.ItemPlaylistvideoBinding
import com.example.home.playlistmodel.Tag
import com.example.ui.BaseRvAdapter
import com.example.ui.BaseRvAdapter.BaseRvViewHolder

class PlayListTagAdapter: BaseRvAdapter<Tag>() {
    inner class PlayListTagViewHolder(itemView: View) : BaseRvViewHolder(itemView) {

        private val binding = ItemPlaylistvideoBinding.bind(itemView)

        fun bind(Data: Tag) {
            binding.btnTag.text = Data.name
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        val binding = ItemPlaylistvideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlayListTagViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {
        if (holder is PlayListTagViewHolder) {
            holder.bind(data[position])

        }
    }
}