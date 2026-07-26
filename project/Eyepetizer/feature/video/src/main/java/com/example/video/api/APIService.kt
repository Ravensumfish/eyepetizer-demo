package com.example.video.api

import com.example.video.model.CommentResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.video.model.RelatedResponse
import retrofit2.http.Url

interface APIService {
    @GET("v4/video/related")
    fun getRelated(@Query("id")videoId:String)
    : Observable<RelatedResponse>

    @GET("v2/replies/video")
    fun getComments(@Query("videoId")videoId: String): Observable<CommentResponse>

    @GET
    fun getNextComments(@Url url:String):Observable<CommentResponse>

}