package com.example.home.homemodel

/**
 * @Desc : 首页视频数据类
 * @Author : zjl
 * @Date : 2026/7/18 11:19
 */

data class HomeData(
    val adExist: Boolean,
    val count: Int,
    val itemList: List<Item>,
    val nextPageUrl: String,
    val total: Int
)

data class Item(
    val adIndex: Int,
    val `data`: Data,
    val id: Int,
    val tag: Any,
    val trackingData: Any,
    val type: String
)

data class Data(
    val actionUrl: String,
    val ad: Boolean,
    val adTrack: List<Any?>,
    val author: Author,
    val brandWebsiteInfo: Any,
    val campaign: Any,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val content: Content,
    val count: Int,
    val cover: CoverX,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: String,
    val duration: Int,
    val favoriteAdTrack: Any,
    val follow: Any,
    val footer: Any,
    val header: Header,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val itemList: List<ItemX>,
    val label: Any,
    val labelList: List<Any?>,
    val lastViewTime: Any,
    val library: String,
    val playInfo: List<PlayInfoXX>,
    val playUrl: String,
    val played: Boolean,
    val playlists: Any,
    val promotion: Any,
    val provider: ProviderXX,
    val reallyCollected: Boolean,
    val recallSource: String,
    val recall_source: String,
    val releaseTime: Long,
    val remark: Any,
    val resourceType: String,
    val searchWeight: Int,
    val shareAdTrack: Any,
    val slogan: Any,
    val src: Int,
    val subTitle: Any,
    val subtitles: List<Any?>,
    val tags: List<TagXX>,
    val text: String,
    val thumbPlayUrl: Any,
    val title: String,
    val titlePgc: String,
    val type: String,
    val videoPosterBean: VideoPosterBeanXX,
    val waterMarks: Any,
    val webAdTrack: Any,
    val webUrl: WebUrlXX
)

data class Author(
    val adTrack: Any,
    val approvedNotReadyVideoCount: Int,
    val description: String,
    val expert: Boolean,
    val follow: Follow,
    val icon: String,
    val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    val name: String,
    val recSort: Int,
    val shield: Shield,
    val videoNum: Int
)

data class Consumption(
    val collectionCount: Int,
    val realCollectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
)

data class Content(
    val adIndex: Int,
    val `data`: DataX,
    val id: Int,
    val tag: Any,
    val trackingData: Any,
    val type: String
)

data class CoverX(
    val blurred: String,
    val detail: String,
    val feed: String,
    val homepage: String,
    val sharing: Any
)

data class Header(
    val actionUrl: String,
    val cover: Any,
    val description: String,
    val font: String,
    val icon: String,
    val iconType: String,
    val id: Int,
    val label: Any,
    val labelList: Any,
    val rightText: String,
    val showHateVideo: Boolean,
    val subTitle: String,
    val subTitleFont: String,
    val textAlign: String,
    val time: Long,
    val title: String
)

data class ItemX(
    val adIndex: Int,
    val `data`: DataXX,
    val id: Int,
    val tag: Any,
    val trackingData: Any,
    val type: String
)

data class PlayInfoXX(
    val height: Int,
    val name: String,
    val type: String,
    val url: String,
    val urlList: List<Url>,
    val width: Int
)

data class ProviderXX(
    val alias: String,
    val icon: String,
    val name: String
)

data class TagXX(
    val actionUrl: String,
    val adTrack: Any,
    val bgPicture: String,
    val childTagIdList: Any,
    val childTagList: Any,
    val communityIndex: Int,
    val desc: String,
    val haveReward: Boolean,
    val headerImage: String,
    val id: Int,
    val ifNewest: Boolean,
    val name: String,
    val newestEndTime: Any,
    val tagRecType: String
)

data class VideoPosterBeanXX(
    val fileSizeStr: String,
    val scale: Double,
    val url: String
)

data class WebUrlXX(
    val forWeibo: String,
    val raw: String
)

data class Follow(
    val followed: Boolean,
    val itemId: Int,
    val itemType: String
)

data class Shield(
    val itemId: Int,
    val itemType: String,
    val shielded: Boolean
)

data class DataX(
    val ad: Boolean,
    val adTrack: List<Any?>,
    val author: Author,
    val brandWebsiteInfo: Any,
    val campaign: Any,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: CoverX,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: String,
    val duration: Int,
    val favoriteAdTrack: Any,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val label: Any,
    val labelList: List<Any?>,
    val lastViewTime: Any,
    val library: String,
    val playInfo: List<PlayInfoXX>,
    val playUrl: String,
    val played: Boolean,
    val playlists: Any,
    val promotion: Any,
    val provider: ProviderXX,
    val reallyCollected: Boolean,
    val recallSource: String,
    val recall_source: String,
    val releaseTime: Long,
    val remark: String,
    val resourceType: String,
    val searchWeight: Int,
    val shareAdTrack: Any,
    val slogan: String,
    val src: Int,
    val subtitles: List<Any?>,
    val tags: List<TagXX>,
    val thumbPlayUrl: Any,
    val title: String,
    val titlePgc: String,
    val type: String,
    val videoPosterBean: VideoPosterBeanXX,
    val waterMarks: Any,
    val webAdTrack: Any,
    val webUrl: WebUrlXX
)

data class Url(
    val name: String,
    val size: Int,
    val url: String
)

data class DataXX(
    val adTrack: List<Any?>,
    val content: ContentX,
    val dataType: String,
    val header: HeaderX
)

data class ContentX(
    val adIndex: Int,
    val `data`: DataXXX,
    val id: Int,
    val tag: Any,
    val trackingData: Any,
    val type: String
)

data class HeaderX(
    val actionUrl: String,
    val cover: Any,
    val description: Any,
    val font: Any,
    val icon: String,
    val iconType: String,
    val id: Int,
    val label: Any,
    val labelList: Any,
    val rightText: Any,
    val showHateVideo: Boolean,
    val subTitle: Any,
    val subTitleFont: Any,
    val textAlign: String,
    val time: Long,
    val title: String
)

data class DataXXX(
    val ad: Boolean,
    val adTrack: List<Any?>,
    val author: Author,
    val brandWebsiteInfo: Any,
    val campaign: Any,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: CoverX,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: Any,
    val duration: Int,
    val favoriteAdTrack: Any,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val label: Any,
    val labelList: List<Any?>,
    val lastViewTime: Any,
    val library: String,
    val playInfo: List<PlayInfoXX>,
    val playUrl: String,
    val played: Boolean,
    val playlists: Any,
    val promotion: Any,
    val provider: ProviderXX,
    val reallyCollected: Boolean,
    val recallSource: Any,
    val recall_source: Any,
    val releaseTime: Long,
    val remark: Any,
    val resourceType: String,
    val searchWeight: Int,
    val shareAdTrack: Any,
    val slogan: Any,
    val src: Any,
    val subtitles: List<Any?>,
    val tags: List<TagXX>,
    val thumbPlayUrl: Any,
    val title: String,
    val titlePgc: Any,
    val type: String,
    val videoPosterBean: VideoPosterBeanXX,
    val waterMarks: Any,
    val webAdTrack: Any,
    val webUrl: WebUrlXX
)