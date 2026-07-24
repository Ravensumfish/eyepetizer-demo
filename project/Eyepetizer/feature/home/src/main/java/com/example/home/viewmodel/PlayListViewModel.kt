package com.example.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.playlistmodel.Data
import com.example.home.playlistmodel.TopicDetailData
import com.example.home.playlistmodel.TopicItemData
import com.example.home.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class PlayListViewModel: ViewModel(){
    private val repository = NetRepository()

    private val _topMessage = MutableLiveData<MutableList<TopicDetailData>>(mutableListOf())
    val topMessage: LiveData<MutableList<TopicDetailData>>
        get() = _topMessage
    private val _playListVideos = MutableLiveData<TopicDetailData>()
    val playListVideos: LiveData<TopicDetailData>
        get() =_playListVideos

    private val _videoTotalList = MutableLiveData<MutableList<Data>>(mutableListOf())
    val videoTotalList: LiveData<MutableList<Data>>
        get() = _videoTotalList

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing


    private var playListId: Int=0
    fun setPlayListId(id: Int){
        this.playListId=id
    }

    fun getPlayListVideos(playListId: Int) {
        _isRefreshing.value = true

        repository.getTopicDetail(playListId)
            .subscribe(object : Observer<TopicDetailData> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.e("CategoryViewModel", "首次加载失败", e)
                    _isRefreshing.value = false
                }
                override fun onComplete() {}

                override fun onNext(t: TopicDetailData) {
                    _videoTotalList.postValue(t.itemList
                        .map { it.data }
                        .filter {  val isVideo = it.dataType == "VideoBeanForClient"
                            isVideo }
                        .toMutableList()
                    )
                    Log.d("CategoryViewModel", "首次加载成功，nextPageUrl=$nextPageUrl")
                    _isRefreshing.value = false
                }
            })
    }
    fun getTopMessage(id: Int){
        repository.getDiscoveryCategories()
            .subscribe(object : Observer<MutableList<TopicDetailData>> {
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
        loadCategoryVideos(categoryId)
    }
}