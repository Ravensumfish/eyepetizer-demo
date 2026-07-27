package com.example.home.discovery.category.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.home.databinding.ItemCategoryBinding
import com.example.home.discovery.category.model.DiscoveryCategoryDataItem
import com.example.ui.BaseRvAdapter

class DiscoveryCategoryAdapter : BaseRvAdapter<DiscoveryCategoryDataItem>() {

    inner class CategoryViewHolder(itemView: View) : BaseRvViewHolder(itemView) {

        private val binding = ItemCategoryBinding.bind(itemView)

        fun bind(categoryData: DiscoveryCategoryDataItem) {
            binding.categoryBtnLabel.text = categoryData.name
        }
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
            val binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return CategoryViewHolder(binding.root)
        }


    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            holder.bind(data[position])

        }
    }

        var itemClick: ((Int, DiscoveryCategoryDataItem) -> Unit)? = null

        init {
            onItemClick = { pos, _ ->
                itemClick?.invoke(pos, data[pos])
            }
        }
    }