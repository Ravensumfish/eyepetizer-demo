/**
 * description: 搜索话题页面
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/23
 */

package com.example.combine.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.combine.search.adapter.ResultTopicAdapter
import com.example.search.databinding.PageSearchResultBinding
import kotlin.getValue

class ResultTopicPage: Fragment() {
    lateinit var binding : PageSearchResultBinding
    private var adapter = ResultTopicAdapter()
    private val viewModel : SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageSearchResultBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initData()
        refresh()
        loadMore()
    }

    fun init(){
        binding.rvSearchResult.adapter = adapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())

    }

    fun initData(){
        viewModel.topicList.observe(viewLifecycleOwner){
                l->
            adapter.submitList(l)
        }
        viewModel.loadTopicResult()
    }

    fun refresh(){

        viewModel.isRefreshing.observe(viewLifecycleOwner){
                b->
            binding.srSearchResult.isRefreshing = b
        }

        binding.srSearchResult.setOnRefreshListener {
            if (binding.srSearchResult.verticalScrollbarPosition == 0
                && viewModel.isRefreshing.value == false){
                viewModel.refreshTopic()
            }
        }

        Log.d("TAG", "refresh:${viewModel.isRefreshing.value} ")
    }

    fun loadMore(){
        if (viewModel.isLoading.value==true)return

        binding.rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastItem = layoutManager.findLastVisibleItemPosition()
                val totalCount = layoutManager.itemCount

                //防重复加载  预加载  只支持向下滑动
                if (viewModel.isLoading.value!=true && lastItem >=totalCount-4 && dy>0){
                    Log.d("TAG", "onScrolledVideo: 正在加载更多")
                    viewModel.loadMoreTopic()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }
}