package com.example.video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.net.utils.RetrofitClient
import com.example.video.api.APIService
import com.example.video.api.Repository
import com.example.video.model.CommentItem
import com.example.video.model.RelatedItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class VideoViewModel: ViewModel() {
    private val api = RetrofitClient.create(APIService::class.java)
    private val repository = Repository(api)
    private val disposable = CompositeDisposable()

    private val _relatedList = MutableLiveData<List<RelatedItem>>()
    val relatedList : LiveData<List<RelatedItem>> = _relatedList

    private val _commentList = MutableLiveData<List<CommentItem>>()
    val commentList : LiveData<List<CommentItem>> = _commentList

    private val _currentBrief = MutableLiveData<RelatedItem?>()
    val currentBrief : LiveData<RelatedItem?> = _currentBrief
    private var id : String = "0"

    fun init(brief: RelatedItem){
        _currentBrief.value = brief
    }

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

    fun toVideo(vid:Int){
        val item = relatedList.value?.find { it.id == vid }?:
        throw IllegalArgumentException("toVideo$vid: 无brief")
        _currentBrief.value = item
        id = vid.toString()
        //清除旧的无用订阅，防止积累过多出错
        disposable.clear()
        loadRelated()
        loadComments()
        Log.d("TAG", "toVideo: 跳转到video:$id")
    }

}