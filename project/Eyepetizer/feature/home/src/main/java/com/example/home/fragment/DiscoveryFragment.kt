package com.example.home.fragment

import androidx.fragment.app.Fragment
import com.example.home.viewmodel.DiscoveryViewModel
import com.example.home.adapter.DiscoveryCategoryAdapter
import com.example.home.adapter.DiscoveryPlayListAdapter
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.example.home.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.FragmentDiscoveryBinding

class DiscoveryFragment : Fragment() {
    // 视图绑定
    private var _binding: FragmentDiscoveryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var vm: DiscoveryViewModel
    private val categoryAdapter = DiscoveryCategoryAdapter()
    private val playListAdapter = DiscoveryPlayListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this)[DiscoveryViewModel::class.java]


        vm.categoryList.observe(viewLifecycleOwner){
            categoryAdapter.submitList(it)
        }
        // 绑定RecyclerView
        binding.rvCategory.layoutManager = object :GridLayoutManager(context,4){
            override fun canScrollVertically(): Boolean = false
            override fun canScrollHorizontally(): Boolean = false
        }
        binding.rvCategory.adapter =categoryAdapter


        vm.topicList.observe(viewLifecycleOwner){
            playListAdapter.submitList(it)
        }

        binding.rvPlaylist.layoutManager = object : LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false){}
        binding.rvPlaylist.adapter = playListAdapter

        // 下拉刷新
        binding.swipeRefresh.setOnRefreshListener {
            vm.refresh()
        }

        // 监听刷新状态
        vm.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        vm.getDiscoveryCategories()
        vm.getDiscoveryTopics()

        categoryAdapter.itemClick={_,item->
            val bundle= bundleOf(
                "id" to item.id,
                "name" to item.name,
                "headerImage" to item.headerImage,
                "description" to item.description,
                "bgPicture" to item.bgPicture,
            )

            findNavController().navigate(R.id.categoryDetailFragment,bundle)
        }

        playListAdapter.itemClick={_,item->
            val bundle= bundleOf(
                "id" to item.id,
                "headerImage" to item.image,
                "description" to item.description,
            )

            findNavController().navigate(R.id.playListFragment,bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}