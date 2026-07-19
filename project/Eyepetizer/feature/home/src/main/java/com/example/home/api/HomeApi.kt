package com.example.home.api

/**
 * @Desc : 首页视频数据类
 * @Author : zjl
 * @Date : 2026/7/18 14:31
 */

import com.example.home.homemodel.HomeData

import retrofit2.http.GET
import io.reactivex.rxjava3.core.Observable

interface HomeApi {
    @GET("v5/index/tab/allRec")
    fun getHomeVideos(): Observable<HomeData>

    @GET("v5/index/tab/allRec")
    fun getMoreVideos(url: String): Observable<HomeData>
}