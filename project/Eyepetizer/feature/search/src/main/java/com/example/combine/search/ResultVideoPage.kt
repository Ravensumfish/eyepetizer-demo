/**
 * description: 搜索视频页面
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
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
import com.example.combine.search.adapter.ResultVideoAdapter
import com.example.search.databinding.PageSearchResultBinding
import com.therouter.TheRouter

class ResultVideoPage : Fragment() {
    lateinit var binding : PageSearchResultBinding
    private var resultAdapter = ResultVideoAdapter()
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
        toVideoDetail()
    }

    fun init(){
        binding.rvSearchResult.adapter = resultAdapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())

    }

    fun initData(){
        viewModel.videoList.observe(viewLifecycleOwner){ l->
            resultAdapter.submitList(l)
        }
        viewModel.loadVideoResult()
        viewModel.loadTopicResult()

    }

    fun toVideoDetail(){
        resultAdapter.onItemClick = {pos,item->
            TheRouter
                .build("/feature/video/VideoActivity")
                .withInt("id",item.videoId.toInt())
                .withString("title",item.title)
                .withString("icon",item.author.avatar.url)
                .withString("name",item.author.nick)
                .withString("category",item.tags[0].title)
                .withString("description",item.text)
                .withString("playUrl",item.playUrl)
                .withInt("collectionCount",item.collection_count)
                .withInt("shareCount",item.share_count)
                .withInt("replyCount",0)
                .navigation()

        }
    }
}