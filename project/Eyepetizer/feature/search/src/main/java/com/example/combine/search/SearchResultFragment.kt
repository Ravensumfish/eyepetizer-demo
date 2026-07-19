package com.example.combine.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.combine.search.adapter.SearchResultRvAdapter
import com.example.search.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {
    lateinit var binding : FragmentSearchResultBinding
    private var resultAdapter = SearchResultRvAdapter()
    private val viewModel : SearchViewModel by activityViewModels()

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
        initData()
    }

    fun init(){
        binding.rvSearchResult.adapter = resultAdapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())

    }

    fun initData(){
        viewModel.resultList.observe(viewLifecycleOwner){l->
            resultAdapter.submitList(l)
        }

        Log.d("TAG", "initData:加载result列表 ")
        viewModel.loadFeed()

    }
}