package com.example.home.fragment

import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.home.viewmodel.HomeViewModel
import com.example.home.adapter.HomeVideoAdapter
import com.example.home.homemodel.Data
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.databinding.FragmentHomeBinding

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater

class HomeFragment : Fragment() {

    // 视图绑定
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var vm: HomeViewModel
    private val adapter = HomeVideoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this)[HomeViewModel::class.java]

        // 绑定RecyclerView
        binding.rvHome.layoutManager = LinearLayoutManager(context)
        binding.rvHome.adapter = adapter


        // 下拉刷新
        binding.swipeRefresh.setOnRefreshListener {
            vm.refresh()
        }

        // 上拉加载
        adapter.onLoadMore = {
            vm.loadMore()
        }

        adapter.onShareClick = { Data ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${Data.playUrl}"
                )
            }
            startActivity(
                Intent.createChooser(
                    intent,
                    "分享到"
                )
            )
        }

            // 监听总数据列表
            vm.videoTotalList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            // 监听刷新状态
            vm.isRefreshing.observe(viewLifecycleOwner) {
                binding.swipeRefresh.isRefreshing = it
            }

            vm.getHomeVideos()

            vm.isLoadMore.observe(viewLifecycleOwner) { isLoading ->
                if (!isLoading) {
                    adapter.setLoadingMore(false)
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
