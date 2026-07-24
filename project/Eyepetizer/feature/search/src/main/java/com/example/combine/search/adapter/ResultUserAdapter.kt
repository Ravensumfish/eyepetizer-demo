package com.example.combine.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.combine.search.model.AuthorItem
import com.example.combine.search.model.UserItem
import com.example.search.R
import com.example.ui.BaseRvAdapter

class ResultUserAdapter : BaseRvAdapter<UserItem>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_author,parent,false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<UserItem>.BaseRvViewHolder,
        position: Int
    ) {
        holder as SearchResultViewHolder
        var item = data[position]
        holder.name.text = item.nick
        holder.description.text = item.description

        Glide.with(holder.itemView.context)
            .load(item.avatar.url)
            .centerCrop()
            .into(holder.img)
    }

    inner class SearchResultViewHolder(item: View) : BaseRvViewHolder(item){
        var img : ImageView = item.findViewById(R.id.img_result_avatar)
        var name : TextView = item.findViewById(R.id.tv_result_author_name)
        val description : TextView = item.findViewById(R.id.tv_result_author_description)
    }
}
