package com.example.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.dailymodel.DailyData
import com.example.home.discoverymodel.DiscoveryCategoryDataItem
import com.example.home.playlistlistmodel.Data
import com.example.home.playlistlistmodel.TopicListData
import com.example.home.playlistmodel.TopicDetailData
import com.example.home.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class DiscoveryViewModel: ViewModel() {
    private val repository = NetRepository()

    private val _categoryList = MutableLiveData<MutableList<DiscoveryCategoryDataItem>>(mutableListOf())
    val categoryList: LiveData<MutableList<DiscoveryCategoryDataItem>>
        get() = _categoryList

    private val _topicList = MutableLiveData<MutableList<Data>>(mutableListOf())
    val topicList: LiveData<MutableList< Data>>
        get() = _topicList

    // ✅ 新增：保存专题详情数据（包含 nextPageUrl）
    private var currentTopic: TopicListData? = null

    // ✅ 新增：当前页数
    private var currentPage = 0

    // ✅ 新增：是否还有更多数据
    private val _hasMore = MutableLiveData(true)
    val hasMore: LiveData<Boolean> = _hasMore

    // ✅ 新增：加载状态
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var playListId: Int = 0
    var nextPageUrl = ""

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    fun setPlayListId(id: Int) {
        this.playListId = id
    }

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
        currentPage = 0
        _isLoading.value = true
        _hasMore.value = true
        _isRefreshing.value = true
        repository.getDiscoveryTopics()
            .subscribe(object : Observer<TopicListData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DiscoveryViewModel", "错误：${e.message}")
                }

                override fun onComplete() { }

                override fun onNext(t: TopicListData) {
                    // 保存完整数据
                    currentTopic = t

                    // 保存 nextPageUrl
                     nextPageUrl  = t.nextPageUrl
                    Log.d("ViewModel", "旧的 nextPageUrl: ${t.nextPageUrl}")
                    // 判断是否还有更多
                    _hasMore.value = !t.nextPageUrl.isNullOrEmpty()


                    val dataList = t.itemList.map { it.data }.toMutableList()
                    _topicList.postValue(dataList)
                    _isRefreshing.value = false
                }
            })
    }

    fun getMoreTopics() {
        Log.d("ViewModel", "=== loadMore 被调用 ===")
        Log.d("ViewModel", "isLoadingMore: ${_isLoading.value}")
        Log.d("ViewModel", "hasMore: ${_hasMore.value}")

        if (_isLoading.value == true || _hasMore.value == false) {
            return
        }

        // 检查是否有 nextPageUrl
        val nextUrl = currentTopic?.nextPageUrl
        if (nextUrl.isNullOrEmpty()) {
            _hasMore.value = false
            return
        }

        _isLoading.value = true

        repository. getMoreTopics(nextUrl)
            .subscribe(object : Observer<TopicListData> {

                override fun onSubscribe(d: Disposable) {
                    Log.e("ViewModel","onSubscribe")}

                override fun onError(e: Throwable) {
                    Log.d("DiscoveryViewModel", "错误：${e.message}")
                }

                override fun onComplete() { }
                override fun onNext(t: TopicListData) {
                    // 更新 nextPageUrl
                    currentTopic = t
                    Log.d("ViewModel", "新的 nextPageUrl: ${t.nextPageUrl}")
                    _hasMore.value = !t.nextPageUrl.isNullOrEmpty()

                    val newList = t.itemList.map { it.data }.toMutableList()


                    val currentList = _topicList.value ?: mutableListOf()
                    currentList.addAll(newList)
                    _topicList.value = currentList

                    _isLoading.value = false
                }

            })

    }

}