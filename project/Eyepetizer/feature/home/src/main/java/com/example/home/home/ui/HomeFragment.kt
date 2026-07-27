package com.example.home.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.home.viewmodel.HomeViewModel
import com.therouter.TheRouter

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

        adapter.onVideoClick={ Data->
            TheRouter
                .build("/feature/video/VideoActivity")
                .withInt("id",Data.id)
                .withString("title",Data.title)
                .withString("name",Data.author.name)
                .withString("icon",Data.author.icon)
                .withString("category",Data.category)
                .withString("description",Data.description)
                .withString("playUrl",Data.playUrl)
                .withInt("collectionCount",Data.consumption.collectionCount)
                .withInt("shareCount",Data.consumption.shareCount)
                .withInt("replyCount",Data.consumption.replyCount)
                .navigation()
            Log.d("HomeFragment", "跳转指令已发送")
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
            binding.rvHome.adapter=null
            _binding = null
            super.onDestroyView()

        }
    }