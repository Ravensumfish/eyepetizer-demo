package com.example.combine.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.combine.search.adapter.ResultUserAdapter
import com.example.search.databinding.PageSearchResultBinding
import kotlin.getValue

class ResultUserPage: Fragment() {
    lateinit var binding : PageSearchResultBinding
    private var adapter = ResultUserAdapter()
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
        viewModel.userList.observe(viewLifecycleOwner){
                l->
            adapter.submitList(l)
        }
        viewModel.loadUgcResult()
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadUgcResult()

    }
}