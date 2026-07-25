/**
 * description: 搜索作者rv适配器
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */

package com.example.combine.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.search.R
import com.example.ui.BaseRvAdapter
import com.google.android.material.button.MaterialButton


class SearchLabelRvAdapter : BaseRvAdapter<String>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRvAdapter<String>.BaseRvViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_label,parent,false)
        return SearchLabelViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BaseRvAdapter<String>.BaseRvViewHolder,
        position: Int) {
        holder as SearchLabelViewHolder
        val item = data[position]
        holder.label.text = item
    }

    inner class SearchLabelViewHolder(item : View) : BaseRvViewHolder(item) {
        var label : MaterialButton = item.findViewById(R.id.search_btn_label)

        init {
            label.setOnClickListener {
                val pos = absoluteAdapterPosition
                Log.d("TAG", "SearchLabelAdapter: 点击了pos=$pos")
                if (pos!= RecyclerView.NO_POSITION){
                    onItemClick?.invoke(pos,data[pos])
                }
            }
        }

    }
}