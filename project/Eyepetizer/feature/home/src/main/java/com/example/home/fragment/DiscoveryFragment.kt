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
import com.therouter.TheRouter

class DiscoveryFragment : Fragment() {
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

      //分类列表
        vm.categoryList.observe(viewLifecycleOwner){
            categoryAdapter.submitList(it)
        }

        binding.rvCategory.layoutManager = object :GridLayoutManager(context,4){
            override fun canScrollVertically(): Boolean = false
            override fun canScrollHorizontally(): Boolean = false
        }
        binding.rvCategory.adapter =categoryAdapter

       //主题播单
        vm.topicList.observe(viewLifecycleOwner){
            Log.d("DiscoveryFragment", "播单数据数量：${it.size}")
            playListAdapter.submitList(it)
        }

        binding.rvPlaylist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlaylist.isNestedScrollingEnabled = false
        binding.rvPlaylist.adapter = playListAdapter


        vm.hasMore.observe(viewLifecycleOwner) { hasMore ->
            if (!hasMore) {
                Log.d("DiscoveryFragment", "没有更多数据了")
            }
        }

        // 监听横向滑动
        binding.rvPlaylist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisiblePosition >= totalItemCount - 1) {
                    Log.d("DiscoveryFragment", "触发加载更多，当前位置: $lastVisiblePosition，总数: $totalItemCount")
                    vm.getMoreTopics()
                }
            }
        })

        // 首次加载数据
        vm.getDiscoveryTopics()

        binding.layoutSearchBar.setOnClickListener {
            TheRouter
                .build("/feature/search/CombinedActivity")
                .navigation()
        }


        vm.getDiscoveryCategories()


        categoryAdapter.itemClick={_,item->
            val bundle= bundleOf("id" to item.id)

            findNavController().navigate(R.id.categoryDetailFragment,bundle)
        }

        playListAdapter.itemClick={_,item->
            val bundle= bundleOf(
                "id" to item.id)

            findNavController().navigate(R.id.playListFragment,bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}