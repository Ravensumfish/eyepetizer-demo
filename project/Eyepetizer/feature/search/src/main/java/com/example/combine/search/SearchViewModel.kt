package com.example.combine.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.api.CombinedAPIService
import com.example.api.CombinedRepository
import com.example.combine.ranking.model.RankListItem
import com.example.combine.search.model.SearchResultItem
import com.example.data.store.SPUtils
import com.example.net.utils.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description: 管理数据
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */
class SearchViewModel : ViewModel() {

    private val api: CombinedAPIService = RetrofitClient.create(CombinedAPIService::class.java)
    private val repository = CombinedRepository(api)

    //管理rx订阅
    private val disposable = CompositeDisposable()

    //搜索历史纪录数据
    private val _recordList = MutableLiveData<List<String>>()
    val recordList : LiveData<List<String>> = _recordList

    //搜索推荐数据
    private val _recommendList = MutableLiveData<List<String>>()
    val recommendList : LiveData<List<String>> = _recommendList

    private val _resultList = MutableLiveData<List<SearchResultItem>>()
    val resultList : LiveData<List<SearchResultItem>> = _resultList

    private val _weeklyRankList = MutableLiveData<List<RankListItem>>()
    val weeklyRankList : LiveData<List<RankListItem>> = _weeklyRankList

    private var query : String?= null


    fun loadRecord(){
        if (SPUtils.getSPContext() == null)return

        val set = SPUtils.getStringSet("record")
        _recordList.value = set.toList()
    }

   fun loadRecommend(){
        val rl = repository.getQueryHot()
            //子线程请求订阅
            .subscribeOn(
                Schedulers.io()
            )
            //主线程更新ui
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                    words ->
                    _recommendList.value = words.toMutableList()
                }
            )

        disposable.add(rl)
    }

    fun loadResult(){
        if (query == null)return

        Log.d("TAG", "loadResult: viewmodel拿到搜索词$query")

       val rl = repository.getSearchResult(query!!)
           .subscribeOn(
               Schedulers.io()
           )
           .observeOn(
               AndroidSchedulers.mainThread()
           )
           .subscribe(
               {
                   items ->
                   _resultList.value = items.toMutableList()
               }

           )
        disposable.add(rl)
        Log.d("TAG", "loadResult: viewmodel得到列表$rl")
        Log.d("TAG", "对比result列表$resultList")
        //得到item的列表，绑定逻辑交给adapter

    }

    fun loadFeed(){
        val l = repository.getFeed()
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {
                    items-> _resultList.value = items.toMutableList()
                    Log.d("TAG", "loadFeed: viewmodel拿到列表，数量${items.size}")
                }
            )
        disposable.add(l)

    }

    fun loadWeeklyRankPreview(){
        val wl = repository.getWeeklyRank()
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {items->
                    if (items.size>5){
                        _weeklyRankList.value = items.toMutableList().take(5)
                    }else{
                        _weeklyRankList.value = items.toMutableList()
                    }

                    Log.d("TAG", "loadWeeklyRankPreview: viewmodel拿到列表，数量${items.size}")
                }
            )
        disposable.add(wl)
    }

    fun setQuery(s : String){
        query = s
    }

}