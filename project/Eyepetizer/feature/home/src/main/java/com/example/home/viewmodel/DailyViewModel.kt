package com.example.home.viewmodel

/**
 * @Desc : 日报的ViewModel
 * @Author : zjl
 * @Date : 2026/7/19 15:00
 */

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.dailymodel.Data
import com.example.home.dailymodel.DailyData
import com.example.home.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class DailyViewModel : ViewModel() {
    private val repository = NetRepository()

    private val _dailyVideos = MutableLiveData<DailyData>()
    val dailyVideos: LiveData<DailyData>
        get() = _dailyVideos

    private val _moreVideos = MutableLiveData<DailyData>()
    val moreVideos: LiveData<DailyData>
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

    fun getDailyVideos() {
        _isRefreshing.value = true
        repository.getDailyVideos()
            .subscribe(object : Observer<DailyData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DailyViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: DailyData) {
                    _dailyVideos.postValue(t)
                    val videoList = t.itemList
                        .mapNotNull { it.data }
                        .filter { outerData ->
                            val targetType = outerData.content?.data?.dataType
                            val isVideo = targetType == "VideoBeanForClient"
                            isVideo
                        }
                        .toMutableList()
                    _videoTotalList.postValue(videoList)
                    nextPageUrl = t.nextPageUrl
                    Log.d("DailyViewModel","网络请求成功")
                    _isRefreshing.value = false
                }
            })
    }

    fun getMoreDailyVideos(url: String) {

        repository.getMoreDailyVideos(url)
            .subscribe(object : Observer<DailyData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DailyViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: DailyData) {
                    _moreVideos.postValue(t)

                    //合并列表
                    val currentList = _videoTotalList.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(t.itemList
                        .map { it.data }
                        .filter { outerData ->
                            val targetType = outerData.content?.data?.dataType
                            val isVideo = targetType == "VideoBeanForClient"
                            isVideo
                        })
                    _videoTotalList.postValue(currentList)
                    nextPageUrl=t.nextPageUrl
                    _isLoadMore.value = false
                }
            })
    }


    //下拉刷新
    fun refresh() {
        _isRefreshing.value = true
        getDailyVideos()
    }

    //加载更多
    fun loadMore() {
        if (_isLoadMore.value == true) return
        if (nextPageUrl.isNullOrBlank()) {
            Log.e("loadMore", "没有更多数据了")
            return
        }
        _isLoadMore.value = true
        getMoreDailyVideos(nextPageUrl)
    }
}