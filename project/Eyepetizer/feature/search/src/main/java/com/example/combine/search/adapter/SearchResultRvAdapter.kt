package com.example.combine.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.search.R
import com.example.combine.search.model.SearchResultItem
import com.example.ui.BaseRvAdapter
import com.example.utils.TimeUtils
import com.google.android.material.button.MaterialButton

class SearchResultRvAdapter : BaseRvAdapter<SearchResultItem>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result,parent,false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<SearchResultItem>.BaseRvViewHolder,
        position: Int
    ) {
       holder as SearchResultViewHolder
        var item = data[position]
        holder.title.text = item.title
        holder.duration.text = TimeUtils.formatDuration(item.duration)

        Glide.with(holder.itemView.context)
            .load(item.cover.feed)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(holder.img)
    }

    inner class SearchResultViewHolder(item: View) : BaseRvViewHolder(item){
        var img : ImageView = item.findViewById(R.id.img_search_result_video)
        var title : TextView = item.findViewById(R.id.tv_search_result_title)
        var duration : MaterialButton = item.findViewById(R.id.btn_search_result_duration)
    }
}