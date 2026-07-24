package com.example.home.api

/**
 * @Desc : 日报视频接口
 * @Author : zjl
 * @Date : 2026/7/18 14:31
 */

import com.example.home.dailymodel.DailyData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface DailyApi {
    @GET("v5/index/tab/feed")
    fun getDailyVideos(): Observable<DailyData>

    @GET
    fun getMoreDailyVideos(@Url url: String): Observable<DailyData>
}