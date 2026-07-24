package com.example.video.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ui.BaseRvAdapter
import com.example.utils.TimeUtils
import com.example.video.R
import com.example.video.model.RelatedItem

class BriefRvAdapter: BaseRvAdapter<RelatedItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BriefViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video_related,parent,false)
        return BriefViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<RelatedItem>.BaseRvViewHolder,
        position: Int
    ) {
        holder as BriefViewHolder
        val item = data[position]
        Log.d("videoId", "视频相关列表item:$position,id: ${item.id}")
        holder.title.text = item.title
        holder.duration.text = TimeUtils.formatDuration(item.duration)
        holder.author.text = item.author?.name ?:"未知作者"

        Glide.with(holder.itemView.context)
            .load(item.author?.icon)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.avatar)

        Glide.with(holder.itemView.context)
            .load(item.cover.feed)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.feed)
    }

    inner class BriefViewHolder(item: View) : BaseRvViewHolder(item){
        val avatar : ImageView = item.findViewById(R.id.img_video_related_avatar)
        val title : TextView = item.findViewById(R.id.tv_video_related_title)
        val author : TextView = item.findViewById(R.id.tv_video_related_author)
        val feed : ImageView = item.findViewById(R.id.img_video_related)
        val duration : TextView = item.findViewById(R.id.tv_video_related_duration)
    }
}