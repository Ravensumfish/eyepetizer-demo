/**
 * description: 管理数据
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */

package com.example.combine.search

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

    private var _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private var query : String?= null
    private var videoPage = 1
    private var authorPage = 1
    private var imagePage = 1
    private var topicPage = 1
    private var userPage = 1
    private var _isRefreshing = MutableLiveData(false)
    val isRefreshing  : LiveData<Boolean> = _isRefreshing
    private var _isLoading = MutableLiveData(false)
    val isLoading  : LiveData<Boolean> = _isLoading




    fun refreshVideo(){
        videoPage = 1
        disposable.clear()
        loadVideoResult()
        _isRefreshing.value = true
    }

    fun refreshAuthor(){
        authorPage = 1
        disposable.clear()
        loadAuthorResult()
        _isRefreshing.value = true

    }

    fun refreshGraphic(){
        imagePage = 1
        disposable.clear()
        loadGraphicResult()
        _isRefreshing.value = true

    }

    fun refreshTopic(){
        topicPage = 1
        disposable.clear()
        loadTopicResult()
        _isRefreshing.value = true

    }

    fun refreshUser(){
        userPage = 1
        disposable.clear()
        loadUgcResult()
        _isRefreshing.value = true

    }

    fun loadMoreVideo(){
        if (_isLoading.value == true)return

        _isLoading.value = true
        loadPage("video")
        Log.d("TAG", "vm: 正在加载更多 video:第 $videoPage 页")
    }

    fun loadMoreAuthor(){
        if (_isLoading.value == true)return
        _isLoading.value = true
        Log.d("TAG", "vm: 正在加载更多 author:第 $authorPage 页")
        loadPage("pgc")

    }

    fun loadMoreGraphic(){
        if (_isLoading.value == true)return
        _isLoading.value = true
        Log.d("TAG", "vm: 正在加载更多 graphic:第 $imagePage 页")
        loadPage("graphic")
    }

    fun loadMoreTopic(){
        if (_isLoading.value == true)return
        _isLoading.value = true
        Log.d("TAG", "vm: 正在加载更多 topic:第 $topicPage 页")
        loadPage("topic")

    }

    fun loadMoreUser(){
        if (_isLoading.value == true)return
        _isLoading.value = true
        Log.d("TAG", "vm: 正在加载更多 user:第 $userPage 页")
        loadPage("ugc")

    }

    fun loadRecord(){
        if (SPUtils.getSPContext() == null)return

        val account = SPUtils.getString("last_account")
        val set = SPUtils.getStringSet("record_$account")
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
                },
                {e->
                    _error.value = e.message
                    Log.d("TAG", "loadRecommend:加载搜索热词失败 ")
                    Log.d("TAG", "loadRecommend:错误${e.message} ")
                }

            )

        disposable.add(rl)
    }

    fun loadPage(type:String){
        Log.d("TAG", "loadPage: 进入")
        val i = when(type){
            "video"->videoPage
            "pgc"->authorPage
            "graphic"->imagePage
            "topic"->topicPage
            "ugc"->userPage
            else -> 0
        }

        if (query == null )return
        val p = repositoryEye.getNextPage(query!!,i,type,KyUdidStore.get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(type){
                        "video"->{
                            videoPage=it
                            if (videoPage != 0) loadVideoResult()

                        }
                        "pgc"->{
                            authorPage = it
                            if (authorPage != 0) loadAuthorResult()
                        }
                        "graphic"-> {
                            imagePage = it
                            if (imagePage != 0) loadGraphicResult()
                        }
                        "topic"->{
                            topicPage=it
                            if (topicPage != 0) loadTopicResult()
                        }
                        "ugc"->{
                            userPage=it
                            if (userPage != 0) loadUgcResult()
                        }
                    }
                },
                {e->
                    _error.value = e.message
                    Log.d("TAG", "loadPage: 加载$type 搜索页数失败")
                    Log.d("TAG", "loadPage: 错误类型${e.message}")
                }
            )
        disposable.add(p)
    }

    fun loadVideoResult(){
        if (query == null )return

        Log.d("TAG", "loadVideoResult: viewmodel拿到搜索词$query")


       val rl = repositoryEye.getSearchResult(query!!,videoPage,"video", KyUdidStore.get())
           .subscribeOn(
               Schedulers.io()
           )
           .observeOn(
               AndroidSchedulers.mainThread()
           )
           .subscribe(
               {
                   items ->

                   if (_isLoading.value == true){
                       val new = items.filterIsInstance<VideoItem>().toMutableList()
                       val current = _videoList.value
                       val l = current?.plus(new)
                       Log.d("TAG", "loadVideoResult: 追加后的列表数量${l?.size}")
                       _videoList.value = l?:mutableListOf()
                   }else{
                       _videoList.value = items.filterIsInstance<VideoItem>().toMutableList()
                   }
                   _isLoading.value = false
                   _isRefreshing.value = false

                   Log.d("TAG", "loadVideoResult: viewmodel得到列表${_videoList.value}")
               },
               {e->
                   _isRefreshing.value = false
                   _isLoading.value = false
                   _error.value = e.message
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


        val rl = repositoryEye.getSearchResult(query!!,authorPage,"pgc", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                    if (_isLoading.value == true){
                        val new = items.filterIsInstance<UserItem>().toMutableList()
                        val current = _authorList.value
                        val l = current?.plus(new)
                        Log.d("TAG", "loadAuthorResult: 追加后的列表数量${l?.size}")
                        _authorList.value = l?:mutableListOf()
                    }else{
                        _authorList.value = items.filterIsInstance<UserItem>().toMutableList()
                    }
                    _isLoading.value = false
                    _isRefreshing.value = false

                    Log.d("TAG", "loadAuthorResult: viewmodel得到列表${_authorList.value}")

                },
                {e->
                    _isRefreshing.value = false
                    _isLoading.value = false
                    _error.value = e.message
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


        val rl = repositoryEye.getSearchResult(query!!,imagePage,"graphic", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                    if (_isLoading.value == true){
                        val new = items.filterIsInstance<ImageItem>().toMutableList()
                        val current = _imageList.value
                        val l = current?.plus(new)
                        Log.d("TAG", "loadGraphicResult: 追加后的列表数量${l?.size}")
                        _imageList.value = l?:mutableListOf()
                    }else{
                        _imageList.value = items.filterIsInstance<ImageItem>().toMutableList()
                    }
                    _isLoading.value = false
                    _isRefreshing.value = false
                    Log.d("TAG", "loadGraphicResult: viewmodel得到列表${_imageList.value}")

                },
                {e->
                    _isRefreshing.value = false
                    _isLoading.value = false
                    _error.value = e.message
                    Log.d("TAG", "loadGraphicResult: 加载搜索结果失败")
                    Log.d("TAG", "loadGraphicResult: 错误类型${e.message}")
                }

            )
        disposable.add(rl)
    }

    fun loadUgcResult(){
        if (query == null)return

        Log.d("TAG", "调用loadUgcResult")
        Log.d("TAG", "loadUgcResult: viewmodel拿到搜索词$query")


        val rl = repositoryEye.getSearchResult(query!!,userPage,"ugc", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                    if (_isLoading.value == true){
                        val new = items.filterIsInstance<UserItem>().toMutableList()
                        val current = _userList.value
                        val l = current?.plus(new)
                        Log.d("TAG", "loadUgcResult: 追加后的列表数量${l?.size}")
                        _userList.value = l?:mutableListOf()
                    }else{
                        _userList.value = items.filterIsInstance<UserItem>().toMutableList()
                    }
                    _isLoading.value = false
                    _isRefreshing.value = false
                    Log.d("TAG", "loadUgcResult: 拿到列表${_userList.value}")
                },
                {e->
                    _isRefreshing.value = false
                    _isLoading.value = false
                    _error.value = e.message
                    Log.d("TAG", "loadUgcResult: 加载搜索结果失败")
                    Log.d("TAG", "loadUgcResult: 错误类型${e.message}")
                }
            )
        disposable.add(rl)
        Log.d("TAG", "结束loadUgcResult")


    }

    fun loadTopicResult(){
        if (query == null)return

        Log.d("TAG", "调用loadTopicResult")
        Log.d("TAG", "loadTopicResult: viewmodel拿到搜索词$query")


        val rl = repositoryEye.getSearchResult(query!!,topicPage,"topic", KyUdidStore.get())
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                        items ->
                    if (_isLoading.value == true){
                        val new = items.filterIsInstance<TopicItem>().toMutableList()
                        val current = _topicList.value
                        val l = current?.plus(new)
                        Log.d("TAG", "loadTopicResult: 追加后的列表数量${l?.size}")
                        _topicList.value = l?:mutableListOf()
                    }else{
                        _topicList.value = items.filterIsInstance<TopicItem>().toMutableList()
                    }
                    _isLoading.value = false
                    _isRefreshing.value = false
                    Log.d("TAG", "loadTopicResult: 拿到列表${_topicList.value}")
                },
                {e->
                    _isRefreshing.value = false
                    _isLoading.value = false
                    _error.value = e.message
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
                },
                {e->
                    _error.value = e.message
                    Log.d("TAG", "loadWeeklyRankPreview:加载失败")
                    Log.d("TAG", "loadWeeklyRankPreview:错误${e.message}")

                }
            )
        disposable.add(wl)
    }

    fun setQuery(s : String){
        query = s
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}