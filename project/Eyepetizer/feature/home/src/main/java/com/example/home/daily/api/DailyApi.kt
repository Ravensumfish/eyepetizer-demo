package com.example.home.daily.api

import com.example.home.daily.dailymodel.DailyData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @Desc : 日报视频接口
 * @Author : zjl
 * @Date : 2026/7/18 14:31
 */

interface DailyApi {
    @GET("v5/index/tab/feed")
    fun getDailyVideos(): Observable<DailyData>

    @GET
    fun getMoreDailyVideos(@Url url: String): Observable<DailyData>
}