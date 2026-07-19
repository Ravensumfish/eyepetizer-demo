package com.example.home.api

import com.example.home.dailymodel.DailyData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface DailyApi {
    @GET("v5/index/tab/feed")
    fun getDailyVideos(): Observable<DailyData>

    @GET("v5/index/tab/feed")
    fun getMoreDailyVideos(url: String): Observable<DailyData>
}