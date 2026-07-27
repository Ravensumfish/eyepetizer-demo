package com.example.home.discovery.playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.discovery.playlist.detailmodel.Data
import com.example.home.discovery.playlist.detailmodel.TopicDetailData
import com.example.home.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class PlayListViewModel: ViewModel(){
    private val repository = NetRepository()

    private val _topMessage = MutableLiveData<TopicDetailData>()
    val topMessage: LiveData<TopicDetailData>
        get() = _topMessage

    private val _playListVideos = MutableLiveData<TopicDetailData>()
    val playListVideos: LiveData<TopicDetailData>
        get() =_playListVideos


    private val _videoTotalList = MutableLiveData<MutableList<Data>>(mutableListOf())
    val videoTotalList: LiveData<MutableList<Data>>
        get() = _videoTotalList

    //下拉刷新
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
                override fun onSubscribe(d: Disposable) { }

                override fun onError(e: Throwable) {
                    Log.e("PlayListViewModel", "首次加载失败", e)
                    _isRefreshing.value = false
                }
                override fun onComplete() {}

                override fun onNext(t: TopicDetailData) {

                    val filteredList = t.itemList
                        .map { it.data }
                        .filter { outerData ->
                            val targetType = outerData.content.data.dataType
                            val isVideo = targetType == "VideoBeanForClient"
                            isVideo
                        }
                        .toMutableList()

                    _videoTotalList.value = filteredList
                    _topMessage.value = t
                    _isRefreshing.value = false
                }

            })

    }


    fun refresh() {
        _isRefreshing.value = true
        getPlayListVideos(playListId)
    }
}