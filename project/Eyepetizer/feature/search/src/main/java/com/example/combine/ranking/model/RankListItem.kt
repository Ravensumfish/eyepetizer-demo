package com.example.combine.ranking.model



data class RankListItem(
    val id:Int,
    val title:String,
    val cover : RankListPicture,
    val author: AuthorDetail,
    val category: String,
    val duration:Int
)

data class AuthorDetail(
    val id: Int,
    val icon : String,
    val name : String
)

data class RankListPicture(
    val feed : String
)


data class RankResponse(
    val itemList : List<RankData>?
)

data class RankData(
    val data : RankListItem?
)