/**
 * description: 搜索接口定义
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */

package com.example.api

import com.example.combine.ranking.model.RankResponse
import com.example.combine.search.model.DebugInfo
import com.example.combine.search.model.SearchResultResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface CombinedAPIService {

    //搜索
    @GET("v3/queries/hot")
    fun getQueryHot(): Observable<List<String>>

    @GET("v1/search/search/get_search_result_v2")
    fun getResultPage(
        @Query("query") query :String,
        @Query("type") type: String,
        @Query("page")page:Int,
        @Query("udid") udid: String
    ): Observable<SearchResultResponse>

    @GET("v1/search/search/get_search_result_v2")
    fun getSearchResult(
        @Query("query") query :String,
        @Query("num") num:Int,
        @Query("type") type: String,
        //设备唯一标识码，不加请求不到数据
        @Query("udid") udid: String
    )
    : Observable<SearchResultResponse>


    //排行榜
    @GET("v4/rankList/videos?strategy=weekly")
    fun getWeeklyRank(): Observable<RankResponse>

    @GET("v4/rankList/videos?strategy=monthly")
    fun getMonthlyRank(): Observable<RankResponse>

    @GET("v4/rankList/videos?strategy=historical")
    fun getHistoricalRank(): Observable<RankResponse>

}