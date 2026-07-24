package com.example.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.store.SPUtils

class SearchViewModel : ViewModel() {
    //搜索历史纪录数据
    private val _recordList = MutableLiveData<List<String>>()
    val recordList : LiveData<List<String>> = _recordList

    //搜索推荐数据
    private val _recommendList = MutableLiveData<List<String>>()
    val recommendList : LiveData<List<String>> = _recommendList


    private fun loadRecord(){

    }

    private fun loadRecommend(){

    }


}