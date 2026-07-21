package com.example.home.api

/**
 * @Desc : 首页视频接口
 * @Author : zjl
 * @Date : 2026/7/21 16:42
 */

import com.example.home.discoverymodel.CategoryData
import com.example.home.discoverymodel.DiscoveryCategoryDataItem
import com.example.home.homemodel.HomeData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Url



interface DiscoveryApi {
    @GET("v4/categories")
    fun getDiscoveryCategories(): Observable<MutableList<DiscoveryCategoryDataItem>>

    @GET
    fun getCategoryDetailVideos(@Url url: String): Observable<CategoryData>

}


