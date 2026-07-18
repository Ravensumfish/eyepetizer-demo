package com.example.home.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

import com.example.home.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import com.example.home.model.HomeData

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

    var nextUrlPager = ""

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
                    _moreVideos.postValue(t)
                    _videoTotalList.postValue(t.itemList.map { it.data }.toMutableList())
                    nextUrlPager = t.nextPageUrl
                    _isRefreshing.value = false
                }
            })
    }

    fun getMoreVideos(url: String) {

        repository.getMoreVideos(url)
            .subscribe(object : Observer<HomeData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("MainViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: HomeData) {
                    _moreVideos.postValue(t)

                    //合并列表
                    val currentList = _videoTotalList.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(t.itemList.map { it.data })
                    _videoTotalList.postValue(currentList)
                    nextUrlPager=t.nextPageUrl
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
        if (_isLoadMore.value == true) return
        _isLoadMore.value = true

        val lastDate = _videoTotalList.value?.lastOrNull()?.actionUrl
        if (lastDate.isNullOrBlank()) {
            _isLoadMore
                .value = false
            return
        }
        getMoreVideos(nextUrlPager)

    }
}