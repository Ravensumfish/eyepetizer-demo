package com.example.home.homeapi

import com.example.home.model.HomeData
import com.example.home.model.Item
import com.example.home.model.Data
import com.example.home.model.Author
import com.example.home.model.Consumption
import com.example.home.model.Content
import com.example.home.model.CoverX
import com.example.home.model.Header
import com.example.home.model.ContentX
import com.example.home.model.DataX
import com.example.home.model.DataXX
import com.example.home.model.DataXXX
import com.example.home.model.WebUrlXX
import com.example.home.model.VideoPosterBeanXX
import com.example.home.model.Url
import com.example.home.model.Shield
import com.example.home.model.TagXX
import com.example.home.model.ProviderXX
import com.example.home.model.Follow
import com.example.home.model.HeaderX
import com.example.home.model.ItemX

import retrofit2.http.GET
import retrofit2.http.Path
import io.reactivex.rxjava3.core.Observable

interface HomeApi {
    @GET("/v5/index/tab/allRec")
    fun getHomeVideos(): Observable<HomeData>

    @GET("/v5/index/tab/allRec")
    fun getMoreVideos(url: String): Observable<HomeData>
}