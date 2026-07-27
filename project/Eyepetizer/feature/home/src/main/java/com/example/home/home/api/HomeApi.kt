package com.example.home.home.api

import com.example.home.home.model.HomeData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @Desc : 首页视频接口
 * @Author : zjl
 * @Date : 2026/7/17 14:31
 */

interface HomeApi {
    @GET("v5/index/tab/allRec")
    fun getHomeVideos(): Observable<HomeData>

    @GET
    fun getMoreVideos(@Url url: String): Observable<HomeData>
}