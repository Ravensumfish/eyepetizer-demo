package com.example.video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.net.utils.RetrofitClient
import com.example.video.api.APIService
import com.example.video.api.Repository
import com.example.video.model.Author
import com.example.video.model.BriefItem
import com.example.video.model.CommentItem
import com.example.video.model.CommentResponse
import com.example.video.model.Consumption
import com.example.video.model.RelatedItem
import com.example.video.model.Tag
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit

class VideoViewModel: ViewModel() {
    private val api = RetrofitClient.create(APIService::class.java)
    private val repository = Repository(api)
    private val disposable = CompositeDisposable()

    private val _relatedList = MutableLiveData<List<RelatedItem>>()
    val relatedList : LiveData<List<RelatedItem>> = _relatedList

    private val _commentList = MutableLiveData<List<CommentItem>>()
    val commentList : LiveData<List<CommentItem>> = _commentList

    private val _brief = MutableLiveData<BriefItem>()
    val brief : LiveData<BriefItem> = _brief
    private var id : String = "0"

    fun setVideoId(i:Int){
        id = i.toString()
    }

    fun loadRelated(){
        val rl = repository.getRelated(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {l->

                    _relatedList.value = l.toMutableList()
                }
            )

        disposable.add(rl)
    }

    fun loadComments(){
        val cl = repository.getComments(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {l->
                    _commentList.value = l.toMutableList()
                },
                {e->
                    Log.d("TAG", "loadComments: $e")
                }
            )
        disposable.add(cl)
    }

    fun loadBrief(): BriefItem{
        val a = Author(
        304573213,
        "http://ali-img.kaiyanapp.com/577bc334615d3f913cf6620dc52717aa.png?image_process=image/auto-orient,1/resize,w_360/format,png/interlace,1/quality,q_80",
          "尼康 nikon 广告精选"
        )
        val c= Consumption(
            1,
            1,
            1,
        )

        val t = listOf(
            Tag("#广告")
        )
        val b = BriefItem(
            277859,
            "2021 年 5 月尼康广告：日常摄影",
            "这是来自尼康的广告，尼克尔变焦镜头打造，非常适合日常摄影，从人像到旅行。",
            a,
            104,
            "http://static.thefair.net.cn/eyepetizer/pgc_video/video_summary/277859.mp4",
            c,
            t
        )
        return b
    }
}