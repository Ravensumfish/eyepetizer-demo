/**
 * description: 搜索话题rv适配器
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
import com.example.combine.search.model.TopicItem
import com.example.search.R
import com.example.ui.BaseRvAdapter

class ResultTopicAdapter  : BaseRvAdapter<TopicItem>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_topic,parent,false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<TopicItem>.BaseRvViewHolder,
        position: Int
    ) {
        holder as SearchResultViewHolder
        var item = data[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.tag.text = item.tags[0].title

        Glide.with(holder.itemView.context)
            .load(item.cover.url)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(holder.img)
    }

    inner class SearchResultViewHolder(item: View) : BaseRvViewHolder(item){
        var img : ImageView = item.findViewById(R.id.img_result_topic_cover)
        var description : TextView = item.findViewById(R.id.tv_result_topic_description)
        var tag : TextView = item.findViewById(R.id.tv_result_topic_tag)
        val title : TextView = item.findViewById(R.id.tv_result_topic_title)
    }
}