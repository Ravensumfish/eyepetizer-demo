package com.example.combine.search.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
//深层嵌套需多个数据类对应
//最内层所需的字段
open class SRItem

data class VideoItem(
    @SerializedName("video_id")
    val videoId: String,
    val title:String,
    val cover : Cover,
    val duration: Duration,
    val tags : List<Tag>,
    val playUrl:String,
    val author:Author,
    val collection_count:Int,
    val share_count:Int,
    val text: String
): SRItem()

data class AuthorItem(
    val avatar : String,
    val name: String,
    val description:String
): SRItem()

data class UserItem(
    val uid:Int,
    val avatar : Cover,
    val nick: String,
    val description:String
): SRItem()

data class ImageItem(
    val cover: Cover,
    val title: String,
    val consumption:Consumption,
    val author:Author
): SRItem()

data class TopicItem(
    val id: Long,
    val title: String,
    val description: String,
    val cover: Cover,
    val tags: List<Tag>

): SRItem()

data class Author(
    val uid:Int,
    val nick: String,
    val description: String,
    val avatar: Cover
)

data class Consumption(
    val like_count:Int,
    val collection_count:Int,
    val comment_count:Int
)

data class Tag(
    val title: String
)

data class Duration(
    val value:Int,
    val text:String
)

data class Cover(
    val url: String
)

//最外层
data class SearchResultResponse(
    val result : ItemList,
)

data class DebugResponse(
    val debug:DebugInfo
)

data class DebugInfo(
    val page:Int,
    val page_count:Int,
    val item_per_page:Int
)

data class ItemList(
    @SerializedName("item_list")
    val itemList: List<MetroData>,
    val last_item_id:Int

)

data class MetroData(
    val type:String,
    @SerializedName("metro_data")
    val metroData : JsonObject
)


