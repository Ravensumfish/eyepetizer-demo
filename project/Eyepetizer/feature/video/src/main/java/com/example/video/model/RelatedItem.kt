package com.example.video.model

import android.os.Parcelable

data class RelatedItem(
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

data class RelatedResponse(
    val itemList: List<RelatedData>
)

data class RelatedData(
    val type:String,
    val data: RelatedItem
)

//brief

data class Tag(
    val title:String?
)


data class Consumption(
    val collectionCount : Int,
    val shareCount:Int,
    val replyCount:Int
)

data class CommentItem(
    val message : String,
    val createTime :Long,
    val user:User,
    val likeCount : Int
)
data class User(
    val uid:Int,
    val nickname:String,
    val avatar : String
)

data class CommentResponse(
    val itemList:List<CommentData>
)
data class CommentData(
    val type:String,
    val data: CommentItem
)