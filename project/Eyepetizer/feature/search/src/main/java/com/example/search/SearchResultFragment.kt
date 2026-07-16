package com.example.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.search.adapter.SearchResultRvAdapter
import com.example.search.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {
    lateinit var binding : FragmentSearchResultBinding
    private var adapter = SearchResultRvAdapter()

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
        binding.rvSearchResult.adapter = adapter
    }
}