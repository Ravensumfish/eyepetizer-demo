package com.example.home.api

/**
 * @Desc : 首页视频接口
 * @Author : zjl
 * @Date : 2026/7/17 14:31
 */

import com.example.home.homemodel.HomeData

import retrofit2.http.GET
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Url

interface HomeApi {
    @GET("v5/index/tab/allRec")
    fun getHomeVideos(): Observable<HomeData>

    @GET
    fun getMoreVideos(@Url url: String): Observable<HomeData>
}