package com.example.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.home.databinding.ItemPlaylistBinding
import com.example.home.databinding.ItemVideoBinding
import com.example.home.discoverymodel.DiscoveryCategoryDataItem
import com.example.home.playlistmodel.TopicItemData
import com.example.ui.BaseRvAdapter


class DiscoveryPlayListAdapter: BaseRvAdapter<TopicItemData>() {


    var onPlayListClick: ((TopicItemData) -> Unit)? = null

    // ViewHolder 类
    inner class VideoViewHolder(itemView: View) : BaseRvViewHolder(itemView) {
        private val binding = ItemPlaylistBinding.bind(itemView)


        fun bind(playListData: TopicItemData) {
            Glide.with(binding.root.context)
                .load(playListData.image)
                .into(binding.ivCover)

            binding.tvDescription.text = playListData.description
        }
    }


    // 获取指定位置的数据
    fun getItem(position: Int):TopicItemData{
        return data[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {
        // 安全类型转换
        if (holder is VideoViewHolder) {
            holder.bind(data[position])
        }
    }
    var itemClick: ((Int, TopicItemData) -> Unit)? = null

    init {
        onItemClick = { pos, _ ->
            itemClick?.invoke(pos, data[pos])
        }
    }
}




