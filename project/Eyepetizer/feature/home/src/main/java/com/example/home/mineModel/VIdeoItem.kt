package com.example.home.mineModel


data class VideoItem(
    val title: String,
    val dataType: String,
    val id: Int,
    var author: Author?,
    val duration: Int,
    val cover:Cover,

    val description :String?,
    val playUrl :String,
    val consumption:Consumption,
    val tags: List<Tag>?
)

data class Cover(
    val feed:String
)

data class Author(
    val id: Int,
    val icon: String,
    val name: String
)
data class Tag(
    val title:String?
)


data class Consumption(
    val collectionCount : Int,
    val shareCount:Int,
    val replyCount:Int
)
