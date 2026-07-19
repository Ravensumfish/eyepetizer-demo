/**
 * description: 搜索接口定义
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */

package com.example.api

import com.example.combine.ranking.model.RankResponse
import com.example.combine.search.model.SearchResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface CombinedAPIService {

    //搜索
    @GET("v3/queries/hot")
    fun getQueryHot(): Observable<List<String>>

    @GET("v3/search")//自动构建v3/search?query=keyword
    fun getSearchResult(@Query("query")keyword:String)
    : Observable<SearchResponse>

    @GET("v5/index/tab/feed")
    fun getFeed(): Observable<SearchResponse>

    //排行榜
    @GET("v4/rankList/videos?strategy=weekly")
    fun getWeeklyRank(): Observable<RankResponse>

    @GET("v4/rankList/videos?strategy=monthly")
    fun getMonthlyRank(): Observable<RankResponse>

    @GET("v4/rankList/videos?strategy=historical")
    fun getHistoricalRank(): Observable<RankResponse>

}