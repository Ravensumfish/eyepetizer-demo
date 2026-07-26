package com.example.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.home.R
import com.example.home.homemodel.Data
import com.example.home.mineModel.VideoItem
import com.example.ui.BaseRvAdapter
import com.example.utils.TimeUtils
import com.google.android.material.button.MaterialButton

class MyStarAdapter : BaseRvAdapter<VideoItem>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_star,parent,false)
        return SearchResultViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: BaseRvAdapter<VideoItem>.BaseRvViewHolder,
        position: Int
    ) {
        holder as SearchResultViewHolder
        var item = data[position]
        holder.title.text = item.title
        holder.duration.text = TimeUtils.formatDuration(item.duration)
        holder.tag.text = item.tags?.firstOrNull()?.title?.let {
            val s = "#$it"
            s
        }

        Glide.with(holder.itemView.context)
            .load(item.cover.feed)
            .placeholder(com.example.ui.R.color.gray)
            .centerCrop()
            .into(holder.img)
    }

    inner class SearchResultViewHolder(item: View) : BaseRvViewHolder(item){
        var img : ImageView = item.findViewById(R.id.img_my_star_video)
        var title : TextView = item.findViewById(R.id.tv_my_star_title)
        var duration : Button = item.findViewById(R.id.btn_my_star_duration)
        var tag : TextView = item.findViewById(R.id.tv_my_star_tag)
    }
}