package com.example.home.repository

/**
 * @Desc : 首页视频数据类
 * @Author : zjl
 * @Date : 2026/7/17 21:26
 */

import com.example.home.api.HomeApi
import com.example.home.api.DailyApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import com.example.net.utils.RetrofitClient
import com.example.home.homemodel.HomeData
import com.example.home.dailymodel.DailyData
class NetRepository {
    private val homeApi: HomeApi= RetrofitClient.create(HomeApi::class.java)
    private val dailyApi: DailyApi= RetrofitClient.create(DailyApi::class.java)

    fun getHomeVideos(): Observable<HomeData> {
        return homeApi.getHomeVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMoreVideos(nextPageUrl: String): Observable<HomeData>{
        return homeApi.getMoreVideos(nextPageUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getDailyVideos(): Observable<DailyData> {
        return dailyApi.getDailyVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMoreDailyVideos(nextPageUrl: String): Observable<DailyData>{
        return dailyApi.getMoreDailyVideos(nextPageUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}