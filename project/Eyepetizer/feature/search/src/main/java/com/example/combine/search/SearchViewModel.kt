/**
 * description: 管理数据
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */

package com.example.combine.search

import android.adservices.topics.Topic
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.api.CombinedAPIService
import com.example.api.CombinedRepository
import com.example.api.KyUdidStore
import com.example.api.SearchRetrofitClient
import com.example.combine.ranking.model.RankListItem
import com.example.combine.search.model.ImageItem
import com.example.combine.search.model.TopicItem
import com.example.combine.search.model.UserItem
import com.example.combine.search.model.VideoItem
import com.example.data.store.SPUtils
import com.example.net.utils.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class SearchViewModel : ViewModel() {

    private val api: CombinedAPIService = RetrofitClient.create(CombinedAPIService::class.java)
    private val apiEye: CombinedAPIService = SearchRetrofitClient.create(CombinedAPIService::class.java)
    private val repository = CombinedRepository(api)
    private val repositoryEye = CombinedRepository(apiEye)

    //管理rx订阅
    private val disposable = CompositeDisposable()

    //搜索历史纪录数据
    private val _recordList = MutableLiveData<List<String>>()
    val recordList : LiveData<List<String>> = _recordList

    //搜索推荐数据
    private val _recommendList = MutableLiveData<List<String>>()
    val recommendList : LiveData<List<String>> = _recommendList

    private val _videoList = MutableLiveData<List<VideoItem>>()
    val videoList : LiveData<List<VideoItem>> = _videoList

    private val _authorList = MutableLiveData<List<UserItem>>()
    val authorList : LiveData<List<UserItem>> = _authorList

    private val _userList = MutableLiveData<List<UserItem>>()
    val userList : LiveData<List<UserItem>> = _userList

    private val _imageList = MutableLiveData<List<ImageItem>>()
    val imageList : LiveData<List<ImageItem>> = _imageList

    private val _topicList = MutableLiveData<List<TopicItem>>()
    val topicList : LiveData<List<TopicItem>> = _topicList

    private val _weeklyRankList = MutableLiveData<List<RankListItem>>()
    val weeklyRankList : LiveData<List<RankListItem>> = _weeklyRankList

    private var query : String?= null
    private var isRefresh = false
    private var videoPage = 1
    private var authorPage = 1
//    private var videoPage = 1
//    private var videoPage = 1
//    private var videoPage = 1
    private var totalPage = 1


    fun refresh(){

    }


    fun loadRecord(){
        if (SPUtils.getSPContext() == null)return

        val set = SPUtils.getStringSet("record")
        _recordList.value = set.toList()
    }

   fun loadRecommend(){
        val rl = repository.getQueryHot()
            //子线程请求订阅
            .subscribeOn(
                Schedulers.io()
            )
            //主线程更新ui
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                    words ->
                    _recommendList.value = words.toMutableList()
                }
            )

        disposable.add(rl)
    }

    fun loadVideoResult(){
        if (query == null )return

        Log.d("TAG", "loadVideoResult: viewmodel拿到搜索词$query")


       val rl = repositoryEye.getSearchResult(query!!,10,"video", KyUdidStore.get())
           .subscribeOn(
               Schedulers.io()
           )
           .observeOn(
               AndroidSchedulers.mainThread()
           )
           .subscribe(
               {
                   items ->
                   _videoList.value = items.filterIsInstance<VideoItem>().toMutableList()
                   Log.d("TAG", "loadVideoResult: viewmodel得到列表${_videoList.value}")
               },
               {e->
                   Log.d("TAG", "loadVideoResult: 加载视频搜索结果失败")
                   Log.d("TAG", "loadVideoResult: 错误类型${e.message}")
               }

           )
        disposable.add(rl)

    }

    fun loadAuthorResult(){
        if (query == null )return
        Log.d("TAG", "调用loadAuthorResult")
        Log.d("TAG", "loadAuthorResult: viewmodel拿到搜索词$query")


        val rl = repositoryEye.getSearchResult(query!!,10,"pgc", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                    _authorList.value = items.filterIsInstance<UserItem>().toMutableList()
                    Log.d("TAG", "loadAuthorResult: viewmodel得到列表${_authorList.value}")

                },
                {e->
                    Log.d("TAG", "loadAuthorResult: 加载作者搜索结果失败")
                    Log.d("TAG", "loadAuthorResult: 错误类型${e.message}")
                }

            )
        disposable.add(rl)
        Log.d("TAG", "结束loadAuthorResult")


    }

    fun loadGraphicResult(){
        if (query == null)return
        Log.d("TAG", "调用loadGraphicResult")
        Log.d("TAG", "loadGraphicResult: viewmodel拿到搜索词$query")


        val rl = repositoryEye.getSearchResult(query!!,10,"graphic", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                    _imageList.value = items.filterIsInstance<ImageItem>().toMutableList()
                    Log.d("TAG", "loadGraphicResult: viewmodel得到列表${_imageList.value}")

                },
                {e->
                    Log.d("TAG", "loadGraphicResult: 加载搜索结果失败")
                    Log.d("TAG", "loadGraphicResult: 错误类型${e.message}")
                }

            )
        Log.d("TAG", "loadGResult: test")
        disposable.add(rl)
    }

    fun loadUgcResult(){
        if (query == null)return

        Log.d("TAG", "调用loadUgcResult")
        Log.d("TAG", "loadUgcResult: viewmodel拿到搜索词$query")


        val rl = repositoryEye.getSearchResult(query!!,10,"ugc", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                        _userList.value = items.filterIsInstance<UserItem>().toMutableList()
                    Log.d("TAG", "loadUgcResult: 拿到列表${_userList.value}")
                },
                {e->
                    Log.d("TAG", "loadUgcResult: 加载搜索结果失败")
                    Log.d("TAG", "loadUgcResult: 错误类型${e.message}")
                }
            )
        disposable.add(rl)
        Log.d("TAG", "结束loadUserResult")


    }

    fun loadTopicResult(){
        if (query == null)return

        Log.d("TAG", "调用loadTopicResult")
        Log.d("TAG", "loadTopicResult: viewmodel拿到搜索词$query")


        val rl = repositoryEye.getSearchResult(query!!,10,"topic", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                    _topicList.value = items.filterIsInstance<TopicItem>().toMutableList()
                    Log.d("TAG", "loadTopicResult: 拿到列表${_topicList.value}")
                },
                {e->
                    Log.d("TAG", "loadTopicResult: 加载搜索结果失败")
                    Log.d("TAG", "loadTopicResult: 错误类型${e.message}")
                }
            )
        disposable.add(rl)
        Log.d("TAG", "结束loadTopicResult")


    }


    fun loadWeeklyRankPreview(){
        val wl = repository.getWeeklyRank()
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {items->
                    if (items.size>5){
                        _weeklyRankList.value = items.toMutableList().take(5)
                    }else{
                        _weeklyRankList.value = items.toMutableList()
                    }

                    Log.d("TAG", "loadWeeklyRankPreview: viewmodel拿到列表，数量${items.size}")
                }
            )
        disposable.add(wl)
    }

    fun setQuery(s : String){
        query = s
    }

}