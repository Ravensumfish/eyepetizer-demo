package com.example.home.adapter

/**
 * @Desc : 首页视频列表的adapter
 * @Author : zjl
 * @Date : 2026/7/18 15:26
 */

import com.example.ui.BaseRvAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.home.databinding.ItemVideoBinding
import com.example.home.homemodel.Data
import androidx.recyclerview.widget.RecyclerView

class HomeVideoAdapter : BaseRvAdapter<Data>() {

    var onLoadMore: (() -> Unit)? = null
    var onVideoClick: ((Data) -> Unit)? = null
    var isLoading = false
    var onShareClick: ((Data) -> Unit)? = null


    // ViewHolder 类
    inner class VideoViewHolder(itemView: View) : BaseRvViewHolder(itemView) {
        private val binding = ItemVideoBinding.bind(itemView)

        init {
            binding.flCover.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onVideoClick?.invoke(getItem(position))
                }
            }
            binding.ivShare.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onShareClick?.invoke(getItem(position))
                }
            }
        }

        fun bind(videoData: Data) {
            // 1. 加载封面
            Glide.with(binding.root.context)
                .load(videoData.cover?.feed)
                .into(binding.ivCover)

            // 2. 视频标题
            binding.tvTitle.text = videoData.title

            // 3. 作者头像
            Glide.with(binding.root.context)
                .load(videoData.author?.icon)
                .circleCrop()
                .into(binding.ivAuthor)

            // 4. 作者名称
            binding.tvAuthorName.text = videoData.author?.name

            // 5. 标签
            binding.tvTag.text = "#${videoData.category}"

            // 6. 时长格式化
            val durationStr = formatDuration(videoData.duration)
            binding.tvInfoDuration.text = durationStr
        }
    }

    // 重置加载状态
    fun setLoadingMore(loading: Boolean) {
        isLoading = loading
    }

    // 获取指定位置的数据
    fun getItem(position: Int): Data {
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

        // 触发加载更多：当滚动到倒数第二个 item 时
        if (!isLoading && position >= itemCount - 2) {
            isLoading = true
            onLoadMore?.invoke()
        }
    }

    // 秒转 00:00 格式
    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }
}

