package com.example.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ui.BaseRvAdapter
import com.google.android.material.button.MaterialButton


class SearchLabelRvAdapter : BaseRvAdapter<String>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRvAdapter<String>.BaseRvViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.search.R.layout.item_search_label,parent,false)
        return SearchLabelViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseRvAdapter<String>.BaseRvViewHolder, position: Int) {
        holder as SearchLabelViewHolder
        val item = data[position]
        holder.label.text = item
    }

    inner class SearchLabelViewHolder(item : View) : BaseRvViewHolder(item) {
        var label : MaterialButton = item.findViewById(com.example.search.R.id.search_btn_label)

    }
}