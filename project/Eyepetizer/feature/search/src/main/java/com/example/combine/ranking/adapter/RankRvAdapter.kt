/**
 * description: 排行榜rv适配器
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/18
 */

package com.example.combine.ranking.adapter

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
import com.example.utils.TimeUtils
import org.w3c.dom.Text

class RankRvAdapter: BaseRvAdapter<RankListItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RankHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rank_list,parent,false)
        return RankHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<RankListItem>.BaseRvViewHolder,
        position: Int
    ) {
        holder as RankHolder
        val item = data[position]
        holder.title.text = item.title
        holder.category.text = item.category
        holder.name.text = item.author.name
        holder.duration.text = TimeUtils.formatDuration(item.duration)

       // Log.d("TAG", "RankAdapter:imgUrl=${item.cover.feed} ")
        Glide.with(holder.itemView.context)
            .load(item.cover.feed)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(holder.feed)

        Glide.with(holder.itemView.context)
            .load(item.author.icon)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(holder.author)
    }

    inner class RankHolder(item: View) : BaseRvViewHolder(item){
        var feed : ImageView = item.findViewById(R.id.img_rank_feed)
        var author : ImageView  = item.findViewById(R.id.img_rank_author)
        var title : TextView = item.findViewById(R.id.tv_rank_title)
        val name : TextView = item.findViewById(R.id.tv_rank_name)
        val category : TextView = item.findViewById(R.id.tv_rank_category)
        val duration : TextView = item.findViewById(R.id.tv_rank_duration)
    }
}