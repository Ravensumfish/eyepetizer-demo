package com.example.video.api

import android.util.Log
import com.example.video.model.Author
import com.example.video.model.CommentItem
import com.example.video.model.CommentResponse
import com.example.video.model.RelatedItem
import io.reactivex.rxjava3.core.Observable

class Repository(private val api: APIService) {

    fun getRelated(videoId:String)
    : Observable<List<RelatedItem>>{
        return api.getRelated(videoId).map {
             response ->
             response.itemList.filter {
                 it.type == "videoSmallCard"
             }.map {
                 it.data
             }.toList()
         }.doOnError {
            Log.d("TAG", "getRelated:请求出错,videoId:$videoId ")
        }
    }

    fun getComments(videoId:String): Observable<List<CommentItem>>{
        return  api.getComments(videoId).map {
            response ->
            Log.d("TAG", "getComments: $response")
            response.itemList.filter {
                it.type == "reply"
            }.map {
                it.data
            }.toList()
        }.doOnError {
            Log.d("TAG", "getComments:请求出错 ")
        }
    }
}