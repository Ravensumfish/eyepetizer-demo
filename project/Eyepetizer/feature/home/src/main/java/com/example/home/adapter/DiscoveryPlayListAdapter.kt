package com.example.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.home.databinding.ItemPlaylistBinding
import com.example.home.playlistlistmodel.Data
import com.example.ui.BaseRvAdapter

class DiscoveryPlayListAdapter: BaseRvAdapter<Data>(){


    inner class PlayListViewHolder(itemView: View) : BaseRvViewHolder(itemView) {
        private val binding = ItemPlaylistBinding.bind(itemView)


        fun bind(Data:Data) {
            // 1. 加载封面
            Glide.with(binding.root.context)
                .load(Data. image)
                .into(binding.ivCover)

            //2. 加载概述文字
            binding.tvDescription.text =Data.description

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        val binding = ItemPlaylistBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlayListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {
        // 安全类型转换
        if (holder is PlayListViewHolder) {
            holder.bind(data[position])
        }
    }
    var itemClick: ((Int, Data) -> Unit)? = null

    init {
        onItemClick = { pos, _ ->
            itemClick?.invoke(pos, data[pos])
        }
    }

}




