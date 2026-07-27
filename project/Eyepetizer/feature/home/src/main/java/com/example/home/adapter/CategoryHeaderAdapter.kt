package com.example.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.home.databinding.ItemCategoryHeaderBinding
import com.example.home.discoverymodel.DiscoveryCategoryDataItem

class CategoryHeaderAdapter :
    ListAdapter<DiscoveryCategoryDataItem, CategoryHeaderAdapter.HeaderViewHolder>(
        object : DiffUtil.ItemCallback<DiscoveryCategoryDataItem>() {
            override fun areItemsTheSame(
                oldItem: DiscoveryCategoryDataItem,
                newItem: DiscoveryCategoryDataItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: DiscoveryCategoryDataItem,
                newItem: DiscoveryCategoryDataItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    inner class HeaderViewHolder(
        private val binding: ItemCategoryHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DiscoveryCategoryDataItem) {
            Glide.with(binding.ivPicture)
                .load(item.headerImage)
                .centerCrop()
                .into(binding.ivPicture)

            binding.tvSecondCategory.text = item.name
            binding.tvDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeaderViewHolder {
        val binding = ItemCategoryHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HeaderViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}