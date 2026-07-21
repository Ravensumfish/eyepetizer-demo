package com.example.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.discoverymodel.CategoryData
import com.example.home.discoverymodel.Data
import com.example.home.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class CategoryViewModel : ViewModel() {
    private val repository = NetRepository()
    val BASE_URL = " http://baobab.kaiyanapp.com/api/"

    private val _categoryVideos = MutableLiveData<CategoryData>()
    val categoryVideos: LiveData<CategoryData>
        get() = _categoryVideos

    private val _moreCategoryVideos = MutableLiveData<CategoryData>()
    val moreVideos: LiveData<CategoryData>
        get() = _moreCategoryVideos

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
    private val URL_END = "&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=Android"

    fun getCategoryVideos(id: Int) {
        val url: String = if (id != -1) {
            "${BASE_URL}v4/categories/videoList?id=${id}${URL_END}"
        } else {
            "${nextPageUrl}${URL_END}"
        }
        _isRefreshing.value = true
        repository.getCategoryDetailVideos(url)
            .subscribe(object : Observer<CategoryData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("MainViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: CategoryData) {
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

    //下拉刷新
    fun refresh() {
        _isRefreshing.value = true
        getCategoryVideos(Int)
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
        getCategoryVideos(-1)
    }
}