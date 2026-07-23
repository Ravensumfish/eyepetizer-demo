package com.example.combine.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.combine.search.model.SRItem
import com.example.combine.search.model.VideoItem
import com.example.search.R
import com.example.ui.BaseRvAdapter
import com.google.android.material.button.MaterialButton

class ResultVideoAdapter : BaseRvAdapter<VideoItem>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_video,parent,false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<VideoItem>.BaseRvViewHolder,
        position: Int
    ) {
       holder as SearchResultViewHolder
        var item = data[position]
        holder.title.text = item.title
        holder.duration.text = item.duration.text
        holder.tag.text = item.tags[0].title

        Glide.with(holder.itemView.context)
            .load(item.cover.url)
            .centerCrop()
            .into(holder.img)
    }

    inner class SearchResultViewHolder(item: View) : BaseRvViewHolder(item){
        var img : ImageView = item.findViewById(R.id.img_search_result_video)
        var title : TextView = item.findViewById(R.id.tv_search_result_title)
        var duration : MaterialButton = item.findViewById(R.id.btn_search_result_duration)
        var tag : TextView = item.findViewById(R.id.tv_search_result_tag)
    }
}