package com.example.home.repository

import com.example.home.homeapi.HomeApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.example.net.utils.RetrofitClient
import com.example.home.model.HomeData

class NetRepository {
    private val homeApi: HomeApi= RetrofitClient.create(HomeApi::class.java)

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


}