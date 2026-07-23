/**
 * description: 统一管理接口
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */


package com.example.api

import android.util.Log
import com.example.combine.ranking.model.RankListItem
import com.example.combine.search.model.AuthorItem
import com.example.combine.search.model.ImageItem
import com.example.combine.search.model.SRItem
import com.example.combine.search.model.TopicItem
import com.example.combine.search.model.UserItem
import com.example.combine.search.model.VideoItem
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable

class CombinedRepository(
    private val api : CombinedAPIService
) {
    fun getQueryHot(): Observable<List<String>> {
        return api.getQueryHot()
    }

    //返回数据过于复杂(层层嵌套)，在api中得到resultResponse之后，使用map解包更为清晰明了且简洁
    fun getSearchResult(  query :String,
                          num:Int,
                          type: String,
                          udid: String)
            : Observable<List<SRItem>> {
        Log.d("TAG", "getSearchResult: api请求$query")
        return api.getSearchResult(query,num,type,udid)
            .doOnError { e->
                Log.d("TAG", "repository:loadSearchResult: 错误:${e.message}")
            }
            .map { response ->
                val pgc = response.result.itemList.filter { it.type =="pgc" }
                val video = response.result.itemList.filter { it.type =="video" }
                val graphic = response.result.itemList.filter { it.type =="image" }
                val ugc = response.result.itemList.filter { it.type =="user" }
                val topic = response.result.itemList.filter { it.type =="topic" }
                Log.d("TAG", "getSearchResult:ALL response:$response ")
                //Log.d("TAG", "getSearchResult:response响应pgc:${pgc} ")
                //Log.d("TAG", "getSearchResult:response响应video:${video} ")
                //Log.d("TAG", "getSearchResult:response响应graphic:${graphic} ")
                //Log.d("TAG", "getSearchResult:response响应user:${ugc} ")
                Log.d("TAG", "getSearchResult:response响应topic:${topic} ")
                response.result.itemList.mapNotNull { i->
                    val gson = Gson()

                    when(i.type){
                        "video"->{
                           val v = gson.fromJson(i.metroData, VideoItem::class.java)
                            //Log.d("TAG", "getSearchResult:Video: $v")
                            v
                        }
                        "user"->{
                            val user=gson.fromJson(i.metroData, UserItem::class.java)
                           // Log.d("TAG", "getSearchResult:User: $user")
                            user
                        }
                        "image"->{
                            val image = gson.fromJson(i.metroData, ImageItem::class.java)
                            //Log.d("TAG", "getSearchResult:Image: $image")
                            image
                        }
                        "topic"->{
                            val topic = gson.fromJson(i.metroData, TopicItem::class.java)
                            Log.d("TAG", "getSearchResult:Topic: $topic")
                            topic
                        }

                        else -> {
                            Log.d("TAG", "getSearchResult:otherType:${i.type}")
                            null
                        }
                    }

                }.toList()
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