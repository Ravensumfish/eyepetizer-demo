package com.example.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.discoverymodel.DiscoveryCategoryDataItem
import com.example.home.playlistmodel.TopicData
import com.example.home.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class DiscoveryViewModel: ViewModel() {
    private val repository = NetRepository()

    private val _categoryList = MutableLiveData<MutableList<TopicData>>(mutableListOf())
    val categoryList: LiveData<MutableList<TopicData>>
        get() = _categoryList

    private val _topicList = MutableLiveData<MutableList<DiscoveryCategoryDataItem>>(mutableListOf())
    val topicList: LiveData<MutableList<DiscoveryCategoryDataItem>>
        get() = _topicList

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    fun refresh() {
        _isRefreshing.value = true
        getDiscoveryCategories()
    }


    fun getDiscoveryCategories() {
        _isRefreshing.value = true
        repository.getDiscoveryCategories()
            .subscribe(object : Observer<MutableList<DiscoveryCategoryDataItem>> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DiscoveryViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(dataList: MutableList<DiscoveryCategoryDataItem>) {
                    _categoryList.postValue(dataList)
                    _isRefreshing.value = false
                }
            })
    }

    fun getDiscoveryTopics() {
        _isRefreshing.value = true
        repository.getDiscoveryTopics()
            .subscribe(object : Observer<MutableList<TopicData>> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DiscoveryViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(topicList: MutableList<TopicData>) {
                    _topicList.postValue(topicList)
                    _isRefreshing.value = false
                }
            })
    }



}