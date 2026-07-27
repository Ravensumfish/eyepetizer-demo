package com.example.home.discovery.category.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.discovery.category.model.CategoryData
import com.example.home.discovery.category.model.Data
import com.example.home.discovery.category.model.DiscoveryCategoryDataItem
import com.example.home.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class CategoryViewModel : ViewModel() {
    private val repository = NetRepository()
    val BASE_URL = "http://baobab.kaiyanapp.com/api/"

    private val _topMessage =
        MutableLiveData<MutableList<DiscoveryCategoryDataItem>>(mutableListOf())
    val topMessage: LiveData<MutableList<DiscoveryCategoryDataItem>>
        get() = _topMessage

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

    private var categoryId: Int=0
    fun setCategoryId(id: Int){
        this.categoryId=id
    }

    fun getCategoryVideos(categoryId: Int) {
        _isRefreshing.value = true
        _isLoadMore.value = false
        val url = "${BASE_URL}v4/categories/videoList?id=${categoryId}${URL_END}"

        repository.getCategoryDetailVideos(url)
            .subscribe(object : Observer<CategoryData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.e("CategoryViewModel", "首次加载失败", e)
                    _isRefreshing.value = false
                    _isLoadMore.value = false
                }
                override fun onComplete() {}

                override fun onNext(t: CategoryData) {
                    _videoTotalList.postValue(t.itemList
                            .map { it.data }
                            .filter {  val isVideo = it.dataType == "VideoBeanForClient"
                                isVideo }
                            .toMutableList()
                    )
                    nextPageUrl = t.nextPageUrl ?: ""
                    Log.d("CategoryViewModel", "首次加载成功，nextPageUrl=$nextPageUrl")
                    _isRefreshing.value = false
                    _isLoadMore.value=false

                }
            })
    }

    fun getMoreCategoryVideos(url: String) {

       val url = "${nextPageUrl}${URL_END}"
        Log.e("LoadMore", "请求加载更多: $url")

        repository.getCategoryDetailVideos(url)
            .subscribe(object : Observer<CategoryData> {
                override fun onError(e: Throwable) {
                    Log.e("CategoryViewModel", "加载更多失败", e)
                    _isLoadMore.value = false
                    _isRefreshing.value = false
                }
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: CategoryData) {
                    val currentList = _videoTotalList.value?.toMutableList() ?: mutableListOf()
                    val newItems = t.itemList
                        .map { it.data }
                        .filter { it.dataType == "VideoBeanForClient" }
                    currentList.addAll(newItems)
                    _videoTotalList.postValue(currentList)
                    nextPageUrl = t.nextPageUrl
                    Log.d("CategoryViewModel", "加载更多成功，nextPageUrl=$nextPageUrl")
                    _isLoadMore.value = false
                }
            })
    }

    fun getTopMessage(id: Int){
        repository.getDiscoveryCategories()
            .subscribe(object : Observer<MutableList<DiscoveryCategoryDataItem>> {
            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {
                Log.d("ViewModel", "错误：${e.message}")
            }

            override fun onComplete() {}

            override fun onNext(dataList: MutableList<DiscoveryCategoryDataItem>) {
                _topMessage.postValue(dataList)
            }
        })
    }


    fun refresh() {
        _isRefreshing.value = true
        getCategoryVideos(categoryId)
    }

    //加载更多
    fun loadMore() {
        if (_isLoadMore.value == true) return
        if (nextPageUrl.isNullOrBlank()) {
            Log.e("loadMore", "没有更多数据了")
            return
        }
        _isLoadMore.value = true
        getMoreCategoryVideos(nextPageUrl)
    }
}