/**
 * description: 搜索图文页面
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/23
 */

package com.example.combine.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onResume() {
        super.onResume()

        viewModel.loadGraphicResult()

    }
}