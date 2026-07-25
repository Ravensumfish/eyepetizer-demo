/**
 * description: 排行榜相关实体类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/18
 */


package com.example.combine.ranking.model


data class RankListItem(
    val id:Int,//视频id
    val title:String,//视频标题
    val author: AuthorDetail,//作者信息
    val category: String,//tag（分类
    val description: String,//视频详情描述
    val playUrl:String,//视频播放url
    val consumption: Consumption,//视频点赞收藏分享数量

    val cover : RankListPicture,//视频封面
    val duration:Int//时长
)

data class Consumption(
    val collectionCount : Int,
    val shareCount:Int,
    val replyCount:Int
)

data class AuthorDetail(
    val id: Int,//作者id
    val icon : String,//作者头像
    val name : String//作者名
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