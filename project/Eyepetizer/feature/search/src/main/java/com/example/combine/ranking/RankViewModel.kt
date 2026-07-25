/**
 * description: 排行榜数据存储与获取
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/18
 */

package com.example.combine.ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.api.CombinedAPIService
import com.example.api.CombinedRepository
import com.example.combine.ranking.model.RankListItem
import com.example.net.utils.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RankViewModel : ViewModel() {
    private val api: CombinedAPIService = RetrofitClient.create(CombinedAPIService::class.java)
    private val repository = CombinedRepository(api)
    private val disposable = CompositeDisposable()
    private val _weeklyList = MutableLiveData<List<RankListItem>>()
    val weeklyList : LiveData<List<RankListItem>> = _weeklyList

    private val _monthlyList = MutableLiveData<List<RankListItem>>()
    val monthlyList : LiveData<List<RankListItem>> = _monthlyList

    private val _historicalList = MutableLiveData<List<RankListItem>>()
    val historicalList : LiveData<List<RankListItem>> = _historicalList


    fun loadWeeklyList(){
        val l = repository.getWeeklyRank()
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {l->
                    _weeklyList.value = l.toMutableList()
                }
            )
        disposable.add(l)
    }

    fun loadMonthlyList(){
        val l = repository.getMonthlyRank()
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {l->
                    _monthlyList.value = l.toMutableList()
                }
            )
        disposable.add(l)
    }

    fun loadHistoricalList(){
        val l = repository.getHistoricalRank()
            .subscribeOn(
                Schedulers.io()
            )
            .observeOn(
                AndroidSchedulers.mainThread()
            )
            .subscribe(
                {l->
                    _historicalList.value = l.toMutableList()
                }
            )
        disposable.add(l)
    }

}