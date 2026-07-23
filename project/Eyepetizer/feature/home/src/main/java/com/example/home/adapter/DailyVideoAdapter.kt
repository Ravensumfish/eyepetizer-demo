package com.example.home.adapter

import com.example.ui.BaseRvAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.home.databinding.ItemVideoBinding
import com.example.home.dailymodel.Data
import com.example.home.dailymodel.Author
import com.example.home.dailymodel.Cover
import androidx.recyclerview.widget.RecyclerView

class DailyVideoAdapter : BaseRvAdapter<Data>() {

    var onLoadMore: (() -> Unit)? = null
    var onVideoClick: ((Data) -> Unit)? = null
    var isLoading = false
    var onShareClick: ((Data) -> Unit)? = null

    // 扩展属性：为了适配数据类字段混乱的问题
    val Data.videoTitle: String
        get() = content?.data?.title ?: ""

    val Data.videoAuthor: Author?
        get() = content?.data?.author

    val Data.videoCategory: String
        get() = content?.data?.category ?: ""

    val Data.videoDuration: Int
        get() = content?.data?.duration ?: 0

    val Data.videoCover: Cover?
        get() = content?.data?.cover


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
                .load(videoData.videoCover?.feed)
                .into(binding.ivCover)

            // 2. 视频标题
            binding.tvTitle.text = videoData.videoTitle

            // 3. 作者头像
            Glide.with(binding.root.context)
                .load(videoData.videoAuthor?.icon)
                .circleCrop()
                .into(binding.ivAuthor)

            // 4. 作者名称
            binding.tvAuthorName.text = videoData.videoAuthor?.name

            // 5. 标签
            binding.tvTag.text = "#${videoData.videoCategory}"

            // 6. 时长格式化
            val durationStr = formatDuration(videoData.videoDuration)
            binding.tvDuration.text = durationStr
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