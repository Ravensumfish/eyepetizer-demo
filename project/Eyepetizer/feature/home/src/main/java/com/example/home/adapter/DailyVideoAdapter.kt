package com.example.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.home.databinding.ItemVideoBinding
import com.example.home.dailymodel.Data


class DailyVideoAdapter : ListAdapter<Data, DailyVideoAdapter.VideoViewHolder>(VideoDiffCallback()) {

    var onLoadMore:(()-> Unit)?=null
    var onVideoClick: ((Data) -> Unit)? = null
    var isLoading=false

    inner class VideoViewHolder(
        private val binding: ItemVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // 封面区域点击，跳转到播放页
            binding.flCover.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onVideoClick?.invoke(getItem(position))
                }
            }
        }

        fun bind(videoData: Data) {
            // 1. 加载封面
            Glide.with(binding.root.context)
                .load(videoData.cover.feed)
                .into(binding.ivCover)

            // 2. 视频标题
            binding.tvTitle.text = videoData.title

            // 3. 作者头像
            Glide.with(binding.root.context)
                .load(videoData.author.icon)
                .circleCrop()
                .into(binding.ivAuthor)

            // 4. 作者名称
            binding.tvAuthorName.text = videoData.author.name

            // 5. 标签
            binding.tvTag.text = "#${videoData.category}"

            // 6. 时长格式化
            val durationStr = formatDuration(videoData.duration)
            binding.tvDuration.text = durationStr
            binding.tvInfoDuration.text = durationStr

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (!isLoading && position==itemCount-2)
        {
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

    // DiffUtil 差分刷新，优化列表性能
    class VideoDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
}