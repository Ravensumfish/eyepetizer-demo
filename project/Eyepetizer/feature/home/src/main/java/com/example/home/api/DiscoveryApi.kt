package com.example.home.api

/**
 * @Desc : 发现接口
 * @Author : zjl
 * @Date : 2026/7/24 14:10
 */

import com.example.home.discoverymodel.CategoryData
import com.example.home.discoverymodel.DiscoveryCategoryDataItem
import com.example.home.homemodel.HomeData
import com.example.home.playlistmodel.TopicItemData
import com.example.home.playlistmodel.TopicDetailData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url



interface DiscoveryApi {
    @GET("v4/categories")
    fun getDiscoveryCategories(): Observable<MutableList<DiscoveryCategoryDataItem>>

    @GET
    fun getCategoryDetailVideos(@Url url: String): Observable<CategoryData>

    @GET("v3/specialTopics")
    fun getTopicList(): Observable<MutableList<TopicItemData>>


    @GET("v3/lightTopics/internal/{id}")
    fun getTopicDetail(@Path("id") id: Int): Observable<TopicDetailData>


}


