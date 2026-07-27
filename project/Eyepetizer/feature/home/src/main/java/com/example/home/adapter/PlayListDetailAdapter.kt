package com.example.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.utils.TimeUtils
import com.example.home.playlistmodel.Data
import com.example.home.databinding.ItemPlaylistVideoBinding
import com.example.ui.BaseRvAdapter
import com.example.home.R
import java.text.SimpleDateFormat
import java.util.Date

class PlayListDetailAdapter: BaseRvAdapter<Data>(){

    var onVideoClick: ((Data) -> Unit)? = null

    var onShareClick: ((Data) -> Unit)? = null


    inner class VideoViewHolder(itemView: View) : BaseRvViewHolder(itemView) {
        private val binding = ItemPlaylistVideoBinding.bind(itemView)

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
                .load(videoData.content?.data?.cover?.feed)
                .into(binding.ivCover)

            // 2. 视频标题
            binding.tvTitle.text = videoData.content?.data?.title

            // 3. 作者头像
            Glide.with(binding.root.context)
                .load(videoData.content.data.author?.icon)
                .circleCrop()
                .into(binding.ivAuthor)

            // 4. 作者名称
            binding.tvAuthor.text = videoData.content.data.author.name


            // 6. 时长格式化
            val durationStr = TimeUtils.formatDuration(videoData.content.data.duration)
            binding.tvDuration.text = durationStr

            //7.发布日期格式化
            val publishStr = TimeUtils.transToDate(videoData.header.time)
            binding.tvPublishTime.text = publishStr+"发布："

            //8.概述
            binding.tvDes.text=videoData.content.data.description

            binding.tvReply.text=videoData.content.data.consumption.replyCount.toString()

            binding.tvCollection.text=videoData.content.data.consumption.collectionCount.toString()

            val llTag = binding.llTag
            // 清空旧标签
            llTag.removeAllViews()

            // 取前3个
            val tagList = videoData.content.data.tags.orEmpty().take(3)

            tagList.forEach { tag ->
                val tagBtn = LayoutInflater.from(binding.root.context)
                    .inflate(R.layout.btn_tag, llTag, false) as Button
                tagBtn.text = tag.name
                llTag.addView(tagBtn)
        }

        }
    }


    // 获取指定位置的数据
    fun getItem(position: Int): Data {
        return data[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        val binding =ItemPlaylistVideoBinding.inflate(
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

    // 秒转 00:00 格式
   /** private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }

    private fun  formatDateMsByYMD(milliseconds: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        return simpleDateFormat.format(Date(milliseconds))
    }*/

    fun setData(newList: List<Data>?) {

        data.clear()
        if (newList != null) {
            data.addAll(newList)
        }
        Log.e("Adapter", "data.size: ${data.size}")
        notifyDataSetChanged()
    }

}
