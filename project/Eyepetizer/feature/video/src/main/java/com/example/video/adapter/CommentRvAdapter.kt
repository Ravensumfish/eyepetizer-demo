package com.example.video.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ui.BaseRvAdapter
import com.example.utils.TimeUtils
import com.example.video.R
import com.example.video.model.CommentItem

class CommentRvAdapter: BaseRvAdapter<CommentItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentsViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video_comments,parent,false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<CommentItem>.BaseRvViewHolder,
        position: Int
    ) { holder as CommentsViewHolder

        val item = data[position]

        holder.name.text = item.user?.nickname?:""
        holder.content.text = item.message
        holder.createTime.text = TimeUtils.transToDate(item.createTime)
        holder.likeCount.text = item.likeCount.toString()
        Glide.with(holder.itemView.context)
            .load(item.user?.avatar?:"")
            .placeholder(com.example.ui.R.color.gray)
            .into(holder.avatar)
    }

    inner class CommentsViewHolder(item: View): BaseRvViewHolder(item){
        val avatar : ImageView = item.findViewById(R.id.img_comments_avatar)
        val name : TextView = item.findViewById(R.id.tv_comments_name)
        val content : TextView = item.findViewById(R.id.tv_comments_content)
        val createTime : TextView = item.findViewById(R.id.tv_comments_create_time)
        val likeCount : TextView = item.findViewById(R.id.tv_comments_likes)
    }
}