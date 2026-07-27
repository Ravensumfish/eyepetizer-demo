package com.example.home.daily.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.databinding.FragmentDailyBinding
import com.example.home.daily.viewmodel.DailyViewModel
import com.therouter.TheRouter

class DailyFragment : Fragment() {
    // 视图绑定
    private var _binding: FragmentDailyBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var vm: DailyViewModel
    private val adapter = DailyVideoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this)[DailyViewModel::class.java]

        // 绑定RecyclerView
        binding.rvDaily.layoutManager = LinearLayoutManager(context)
        binding.rvDaily.adapter = adapter


        // 下拉刷新
        binding.swipeRefresh.setOnRefreshListener {
            vm.refresh()
        }

        binding.ivSearch.setOnClickListener {
            Log.d("TAG", "ivSearch 被点击了！！！")
            TheRouter
                .build("/feature/search/CombinedActivity")
                .navigation()
        }

        // 上拉加载
        adapter.onLoadMore = {
            vm.loadMore()
        }

        adapter.onVideoClick={ Data->
            TheRouter
                .build("/feature/video/VideoActivity")
                .withInt("id",Data.content.data.id)
                .withString("title",Data.content.data.title)
                .withString("name",Data.content.data.author.name)
                .withString("icon",Data.content.data.author.icon)
                .withString("category",Data. content.data.category)
                .withString("description",Data.header.description)
                .withString("playUrl",Data.content.data.playUrl)
                .withInt("collectionCount",Data.content.data.consumption.collectionCount)
                .withInt("shareCount",Data.content.data.consumption.shareCount)
                .withInt("replyCount",Data.content.data.consumption.replyCount)
                .navigation()
        }


        //分享功能
        adapter.onShareClick = { Data ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${Data.content.data.playUrl}"
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
        vm.videoTotalList.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                Log.d("DailyFragment", "列表为空")
            } else {
                adapter.submitList(list)
            }
        }

        // 监听刷新状态
        vm.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        vm.getDailyVideos()

        vm.isLoadMore.observe(viewLifecycleOwner){isLoading->
            if (!isLoading){
                adapter.setLoadingMore(false)
            }
        }
    }

    override fun onDestroyView() {
        binding.rvDaily.adapter=null
        _binding = null
        super.onDestroyView()

    }
}