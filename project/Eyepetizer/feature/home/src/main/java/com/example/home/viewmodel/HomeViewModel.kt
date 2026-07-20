package com.example.home.viewmodel

/**
 * @Desc : 首页的ViewModel
 * @Author : zjl
 * @Date : 2026/7/18 15:00
 */

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.homemodel.Data
import com.example.home.homemodel.HomeData
import com.example.home.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class HomeViewModel : ViewModel() {
    private val repository = NetRepository()

    private val _homeVideos = MutableLiveData<HomeData>()
    val homeVideos: LiveData<HomeData>
        get() = _homeVideos

    private val _moreVideos = MutableLiveData<HomeData>()
    val moreVideos: LiveData<HomeData>
        get() = _moreVideos

    private val _videoTotalList = MutableLiveData<MutableList<Data>>(mutableListOf())
    val videoTotalList: LiveData<MutableList<Data>>
        get() = _videoTotalList

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _isLoadMore = MutableLiveData<Boolean>(false)
    val isLoadMore: LiveData<Boolean>
        get() = _isLoadMore

    var nextPageUrl = ""

    fun getHomeVideos() {
        _isRefreshing.value = true
        repository.getHomeVideos()
            .subscribe(object : Observer<HomeData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("MainViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: HomeData) {
                    _homeVideos.postValue(t)
                    _videoTotalList.postValue(t.itemList
                        .map { it.data }
                        .filter { val isVideo = it.dataType == "VideoBeanForClient"
                            Log.d("jia", "过滤: dataType=${it.dataType}, 是否保留=$isVideo")
                            isVideo
                        }
                        .toMutableList())
                    nextPageUrl = t.nextPageUrl
                    Log.d("HomeViewModel","$nextPageUrl")
                    _isRefreshing.value = false
                }
            })
    }

    fun getMoreVideos(url: String) {
        Log.e("HomeViewModel","请求时使用的url:$url")

        repository.getMoreVideos(url)
            .subscribe(object : Observer<HomeData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("HomeViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: HomeData) {
                    _moreVideos.postValue(t)
                    val rawNextUrl=t.nextPageUrl
                    Log.d("HomeViewModel","接口返回的，$rawNextUrl")
                    if(rawNextUrl==url)
                    {
                        Log.d("HomeViewModel","错误")}
                    else{
                        Log.d("HomeViewModel","正确")}
                    nextPageUrl=rawNextUrl
                    Log.d("HomeViewModel","加载更多成功，$nextPageUrl")

                    //合并列表
                    val currentList = _videoTotalList.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(t.itemList
                        .map { it.data }
                        .filter { it.dataType=="VideoBeanForClient" })
                    _videoTotalList.postValue(currentList)

                    _isLoadMore.value = false
                }
            })
    }

    //下拉刷新
    fun refresh() {
        _isRefreshing.value = true
        getHomeVideos()
    }

    //加载更多
    fun loadMore() {
        Log.e("分页地址", nextPageUrl)
        if (_isLoadMore.value == true) return
        if (nextPageUrl.isNullOrBlank()) {
            Log.e("loadMore", "没有更多数据了")
            return
        }
        _isLoadMore.value = true
        getMoreVideos(nextPageUrl)
    }
}