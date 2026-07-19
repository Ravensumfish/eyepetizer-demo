/**
 * description: 统一管理接口
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */


package com.example.api

import android.util.Log
import com.example.combine.ranking.model.RankListItem
import com.example.combine.search.model.SearchResultItem
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Query

class CombinedRepository(
    private val api : CombinedAPIService
) {
    fun getQueryHot(): Observable<List<String>> {
        return api.getQueryHot()
    }

    //返回数据过于复杂(层层嵌套)，在api中得到resultResponse之后，使用map解包更为清晰明了且简洁
    fun getSearchResult(@Query("query")keyword:String)
            : Observable<List<SearchResultItem>> {
        Log.d("TAG", "getSearchResult: api请求$keyword")
        return api.getSearchResult(keyword)
            .map { response ->
                Log.d("TAG", "getSearchResult:response响应$response ")
                response.itemList?.mapNotNull {
                    it.data?.content?.data
                }?.toList()?:emptyList()
            }
    }

    fun getFeed(): Observable<List<SearchResultItem>> {
        return api.getFeed()
            .map { response ->
                Log.d("TAG", "getSearchResult:response响应$response ")
                response.itemList?.mapNotNull {
                    it.data?.content?.data
                }?.toList()?:emptyList()
            }
    }

    fun getWeeklyRank(): Observable<List<RankListItem>> {
        return api.getWeeklyRank().map { response ->
            Log.d("TAG", "getWeeklyRank:response响应$response ")
            response.itemList?.mapNotNull {
                it.data
            }?.toList()?:emptyList()
        }
    }

    fun getMonthlyRank(): Observable<List<RankListItem>> {
        return api.getMonthlyRank().map { response ->
            Log.d("TAG", "getMonthlyRank:response响应$response ")
            response.itemList?.mapNotNull {
                it.data
            }?.toList()?:emptyList()
        }
    }

    fun getHistoricalRank(): Observable<List<RankListItem>> {
        return api.getHistoricalRank().map { response ->
            Log.d("TAG", "getHistoricalRank:response响应$response ")
            response.itemList?.mapNotNull {
                it.data
            }?.toList()?:emptyList()
        }
    }
}