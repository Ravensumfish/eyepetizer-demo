/**
 * description: 搜索图文页面
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
import com.example.combine.search.adapter.ResultImageAdapter
import com.example.search.databinding.PageSearchResultBinding
import kotlin.getValue

class ResultImagePage : Fragment() {
    lateinit var binding : PageSearchResultBinding
    private var adapter = ResultImageAdapter()
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
        onError()
    }

    fun init(){
        binding.rvSearchResult.adapter = adapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())

    }

    fun initData(){
        viewModel.imageList.observe(viewLifecycleOwner){
            l->
            adapter.submitList(l)
        }
        viewModel.loadGraphicResult()
    }

    fun refresh(){

        viewModel.isRefreshing.observe(viewLifecycleOwner){
                b->
            binding.srSearchResult.isRefreshing = b
        }

        binding.srSearchResult.setOnRefreshListener {
            if (binding.srSearchResult.verticalScrollbarPosition == 0
                && viewModel.isRefreshing.value == false){
                viewModel.refreshGraphic()
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
                    viewModel.loadMoreGraphic()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    fun onError(){
        viewModel.error.observe(viewLifecycleOwner){
            binding.srSearchResult.visibility = View.GONE
            binding.noNet.visibility = View.VISIBLE
        }
    }
}