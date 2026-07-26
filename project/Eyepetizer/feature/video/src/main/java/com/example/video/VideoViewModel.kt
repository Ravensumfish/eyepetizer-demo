package com.example.video


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.net.utils.RetrofitClient
import com.example.video.api.APIService
import com.example.video.api.Repository
import com.example.video.model.CommentItem
import com.example.video.model.RelatedItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class VideoViewModel: ViewModel() {
    private val api = RetrofitClient.create(APIService::class.java)
    private val repository = Repository(api)
    private val disposable = CompositeDisposable()

    private val _relatedList = MutableLiveData<List<RelatedItem>>()
    val relatedList : LiveData<List<RelatedItem>> = _relatedList

    private val _commentList = MutableLiveData<List<CommentItem>>()
    val commentList : LiveData<List<CommentItem>> = _commentList

    private val _currentBrief = MutableLiveData<RelatedItem?>()
    val currentBrief : LiveData<RelatedItem?> = _currentBrief
    private var id : String = "0"
    private var url : String? = null
    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean> = _isLoading

    private var _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing : LiveData<Boolean> = _isRefreshing

    fun init(brief: RelatedItem){
        _currentBrief.value = brief
    }

    fun refreshComments(){
        if (_isRefreshing.value == true)return
        _isRefreshing.value = true
        disposable.clear()
        loadComments()
    }

    fun loadMoreComments(){
        if (_isLoading.value == true)return
        if (url == null)return

        _isLoading.value = true
        val l = repository.getNextComments(url!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response->
                   val new = response.itemList.filter {
                       it.type == "reply"
                   }.map {
                        it.data
                    }.toMutableList()

                    if (_isLoading.value == true){
                        val current = _commentList.value
                        val l = current?.plus(new)
                        _commentList.value = l
                    }else{
                        _commentList.value = new
                    }
                    url = response.nextPageUrl
                    _isLoading.value = false

                    Log.d("TAG", "loadMoreComments: 得到列表${_commentList.value}")
                    Log.d("TAG", "loadMoreComments: 得到列表大小${_commentList.value?.size}")
                    Log.d("TAG", "loadComments: 下一页的url是：$url")

                },
                {
                    _isLoading.value = false

                    Log.d("TAG", "loadMoreComments: 加载更多失败")
                }
            )
        disposable.add(l)

    }


    fun sortBy(hot: Boolean){
       val l = if (hot){
            _commentList.value?.sortedByDescending { it.likeCount }?:return
        }else{
            _commentList.value?.sortedByDescending{ it.createTime }?:return
        }
        _commentList.value = l
    }

    fun setVideoId(i:Int){
        id = i.toString()
    }
    fun getVideoId():Int{
        return id.toInt()
    }

    fun loadRelated(){
        val rl = repository.getRelated(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {l->

                    _relatedList.value = l.toMutableList()
                },
                {
                    Log.d("TAG", "loadRelated:id:$id ")
                }
            )

        disposable.add(rl)
    }

    fun loadComments(){
        val cl = repository.getComments(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response->
                   val l = response.itemList.filter {
                             it.type == "reply"
                     }.map {
                        it.data
                    }.toMutableList()
                    url = response.nextPageUrl
                    Log.d("TAG", "loadComments: 下一页的url是：$url")
                    _commentList.value = l
                    Log.d("TAG", "loadComments: 得到列表${_commentList.value}")
                    sortBy(false)
                    _isRefreshing.value = false

                },
                {e->
                    _isRefreshing.value = false
                    Log.d("TAG", "loadComments: $e")
                }
            )
        disposable.add(cl)
    }

    fun toVideo(vid:Int){
        val item = relatedList.value?.find { it.id == vid }?:
        throw IllegalArgumentException("toVideo$vid: 无brief")
        _currentBrief.value = item
        id = vid.toString()
        //清除旧的无用订阅，防止积累过多出错
        disposable.clear()
        loadRelated()
        loadComments()
        Log.d("TAG", "toVideo: 跳转到video:$id")
    }



    //mock
  //  val a = Author(
//        304573213,
//        "http://ali-img.kaiyanapp.com/577bc334615d3f913cf6620dc52717aa.png?image_process=image/auto-orient,1/resize,w_360/format,png/interlace,1/quality,q_80",
//          "尼康 nikon 广告精选"
//        )
//        val c= Consumption(
//            1,
//            1,
//            1,
//        )
//
//        val t = listOf(
//            Tag("#广告")
//        )
//        val b = BriefItem(
//            277859,
//            "2021 年 5 月尼康广告：日常摄影",
//            "这是来自尼康的广告，尼克尔变焦镜头打造，非常适合日常摄影，从人像到旅行。",
//            a,
//            104,
//            "http://static.thefair.net.cn/eyepetizer/pgc_video/video_summary/277859.mp4",
//            c,
//            t
//        )
}