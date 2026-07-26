package com.example.combine.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.combine.ranking.model.RankListItem
import com.example.search.R
import com.example.ui.BaseRvAdapter

class RankPreviewAdapter : BaseRvAdapter<RankListItem>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RankHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_rk,parent,false)
        return RankHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<RankListItem>.BaseRvViewHolder,
        position: Int
    ) {
        holder as RankHolder
        val item = data[position]
        holder.title.text = item.title

        Log.d("TAG", "RankAdapter:imgUrl=${item.cover.feed} ")
        Glide.with(holder.itemView.context)
            .load(item.cover.feed)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(holder.feed)

    }

    inner class RankHolder(item: View) : BaseRvViewHolder(item){
        var feed : ImageView = item.findViewById(R.id.img_search_rk)
        var title : TextView = item.findViewById(R.id.tv_search_rk)
    }
}