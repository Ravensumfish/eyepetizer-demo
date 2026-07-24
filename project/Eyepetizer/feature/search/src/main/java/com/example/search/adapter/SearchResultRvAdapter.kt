package com.example.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.search.R
import com.example.search.model.SearchResultModel
import com.example.ui.BaseRvAdapter
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text

class SearchResultRvAdapter : BaseRvAdapter<SearchResultModel>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.search.R.layout.item_search_result,parent,false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<SearchResultModel>.BaseRvViewHolder,
        position: Int
    ) {
       holder as SearchResultViewHolder
        var item = data[position]
        holder.title.text = item.title
        holder.duration.text = item.duration.toString()

    }

    inner class SearchResultViewHolder(item: View) : BaseRvViewHolder(item){
        var img : ImageView = item.findViewById(R.id.img_search_result_video)
        var title : TextView = item.findViewById(R.id.tv_search_result_title)
        var duration : MaterialButton = item.findViewById(R.id.btn_search_result_duration)
    }
}