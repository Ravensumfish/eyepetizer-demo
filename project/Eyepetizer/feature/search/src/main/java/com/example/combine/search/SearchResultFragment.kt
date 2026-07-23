package com.example.combine.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.combine.search.adapter.ResultVp2Adapter
import com.example.search.databinding.FragmentSearchResultBinding
import com.example.search.databinding.PageSearchResultBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchResultFragment : Fragment(){

    lateinit var pageAdapter : ResultVp2Adapter
    lateinit var binding : FragmentSearchResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        binding.vp2SearchResult.isSaveEnabled = false
        pageAdapter = ResultVp2Adapter(this)
        binding.vp2SearchResult.adapter = pageAdapter
        TabLayoutMediator(binding.tabResult,binding.vp2SearchResult){
                tab,pos->
            tab.text = when(pos){
                0->"视频"
                1->"作者"
                2->"图文"
                3->"话题"
                4->"用户"
                else -> ""
            }
        }.attach()
    }
}