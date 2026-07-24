package com.example.combine.search.model
//深层嵌套需多个数据类对应
//最内层所需的字段
data class SearchResultItem(
    val id: Int,
    val title:String,
    val cover : SearchCover,
    val duration:Int
)

data class SearchCover(
    val feed:String
)

data class SearchItemData(val data : SearchResultItem?)

data class SearchContent(val content : SearchItemData?)

data class SearchData(val data:SearchContent?)

//最外层
data class SearchResponse(val itemList : List<SearchData>?)


