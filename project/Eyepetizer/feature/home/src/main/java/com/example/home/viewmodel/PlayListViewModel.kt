package com.example.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.home.playlistmodel.Data
import com.example.home.playlistmodel.TopicDetailData
import com.example.home.repository.NetRepository
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

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing


    private var playListId: Int=0
    fun setPlayListId(id: Int){
        this.playListId=id
    }


    fun getPlayListVideos(playListId: Int) {
        Log.e("gpl", "整个方法用了, id: $playListId")
        _isRefreshing.value = true

        repository.getTopicDetail(playListId)
            .subscribe(object : Observer<TopicDetailData> {
                override fun onSubscribe(d: Disposable) { Log.e("gpl","走了")}

                override fun onError(e: Throwable) {
                    Log.e("CategoryViewModel", "首次加载失败", e)
                    _isRefreshing.value = false
                }
                override fun onComplete() {}

                override fun onNext(t: TopicDetailData) {
                    Log.e("zhangpl", "onNext 走了")

                    val filteredList = t.itemList
                        .map { it.data }
                        .filter { outerData ->
                            val targetType = outerData.content.data.dataType
                            val isVideo = targetType == "VideoBeanForClient"
                            Log.d("日志", "过滤: dataType=$targetType, 是否保留=$isVideo")
                            isVideo
                        }
                        .toMutableList()

                    _videoTotalList.value = filteredList
                    _topMessage.value = t
                    _isRefreshing.value = false

                    Log.e("zhangpl", "postValue 完成")

                    Log.e("zhangpl", "LiveData 当前值: ${_videoTotalList.value?.size}")

                }

            })

    }


    fun refresh() {
        _isRefreshing.value = true
        getPlayListVideos(playListId)
    }
}