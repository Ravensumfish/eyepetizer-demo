package com.example.home.discovery

import com.example.home.discovery.category.model.CategoryData
import com.example.home.discovery.category.model.DiscoveryCategoryDataItem
import com.example.home.discovery.playlist.listmodel.TopicListData
import com.example.home.discovery.playlist.detailmodel.TopicDetailData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * @Desc : 发现接口
 * @Author : zjl
 * @Date : 2026/7/24 14:10
 */

interface DiscoveryApi {
    @GET("v4/categories")
    fun getDiscoveryCategories(): Observable<MutableList<DiscoveryCategoryDataItem>>

    @GET
    fun getCategoryDetailVideos(@Url url: String): Observable<CategoryData>

    @GET("v3/specialTopics")
    fun getTopicList(): Observable<TopicListData>

    @GET
    fun getMoreTopics(@Url url: String): Observable<TopicListData>


    @GET("v3/lightTopics/internal/{id}")
    fun getTopicDetail(@Path("id") id: Int): Observable<TopicDetailData>


}