package com.example.home

import com.example.home.daily.api.DailyApi
import com.example.home.daily.dailymodel.DailyData
import com.example.home.discovery.DiscoveryApi
import com.example.home.discovery.category.model.CategoryData
import com.example.home.discovery.category.model.DiscoveryCategoryDataItem
import com.example.home.discovery.playlist.listmodel.TopicListData
import com.example.home.discovery.playlist.detailmodel.TopicDetailData
import com.example.home.home.api.HomeApi
import com.example.home.home.model.HomeData
import com.example.net.utils.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class NetRepository {
    private val homeApi: HomeApi = RetrofitClient.create(HomeApi::class.java)
    private val dailyApi: DailyApi = RetrofitClient.create(DailyApi::class.java)

    private val discoveryApi: DiscoveryApi = RetrofitClient.create(DiscoveryApi::class.java)

    fun getHomeVideos(): Observable<HomeData> {
        return homeApi.getHomeVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMoreVideos(nextPageUrl: String): Observable<HomeData> {
        return homeApi.getMoreVideos(nextPageUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getDailyVideos(): Observable<DailyData> {
        return dailyApi.getDailyVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMoreDailyVideos(nextPageUrl: String): Observable<DailyData> {
        return dailyApi.getMoreDailyVideos(nextPageUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getDiscoveryCategories(): Observable<MutableList<DiscoveryCategoryDataItem>> {
        return  discoveryApi.getDiscoveryCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCategoryDetailVideos(nextPageUrl: String): Observable<CategoryData> {
        return discoveryApi.getCategoryDetailVideos(nextPageUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }



    fun getDiscoveryTopics(): Observable<TopicListData> {
        return  discoveryApi.getTopicList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTopicDetail(id: Int): Observable<TopicDetailData> {
        return discoveryApi. getTopicDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMoreTopics(nextPageUrl: String): Observable<TopicListData> {
        return discoveryApi.getMoreTopics(nextPageUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}