package com.example.home.discovery.category.model

data class DiscoveryCategoryDataItem(
    val alias: Any="",
    val bgColor: String="",
    val bgPicture: String="",
    val defaultAuthorId: Int=0,
    val description: String,
    val headerImage: String,
    val id: Int=0,
    val name: String="",
    val tagId: Int=0
)