/**
 * description: 搜索图文rv适配器
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/23
 */

package com.example.combine.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.combine.search.model.ImageItem
import com.example.search.R
import com.example.ui.BaseRvAdapter

class ResultImageAdapter  : BaseRvAdapter<ImageItem>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_image,parent,false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<ImageItem>.BaseRvViewHolder,
        position: Int
    ) {
        holder as SearchResultViewHolder
        var item = data[position]
        holder.title.text = item.title
        holder.likes.text = item.consumption.like_count.toString()
        holder.stars.text = item.consumption.collection_count.toString()

        Glide.with(holder.itemView.context)
            .load(item.cover.url)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(holder.img)
    }

    inner class SearchResultViewHolder(item: View) : BaseRvViewHolder(item){
        var img : ImageView = item.findViewById(R.id.img_result_topic_cover)
        var likes : TextView = item.findViewById(R.id.tv_result_image_likes)
        var stars : TextView = item.findViewById(R.id.tv_result_image_stars)
        val title : TextView = item.findViewById(R.id.tv_result_image_title)
    }
}