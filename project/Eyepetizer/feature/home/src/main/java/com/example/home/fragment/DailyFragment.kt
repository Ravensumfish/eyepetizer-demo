package com.example.home.fragment

import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.home.viewmodel.DailyViewModel
import com.example.home.adapter.DailyVideoAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.databinding.FragmentDailyBinding
import android.util.Log
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.home.dailymodel.Data

class DailyFragment : Fragment() {
    // 视图绑定
    private var _binding: FragmentDailyBinding? = null
    private val binding
        get() = _binding!!

    val Data.videoplayUrl: String?
        get() = content?.data?.playUrl

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

        // 上拉加载
        adapter.onLoadMore = {
            vm.loadMore()
        }

        //分享功能
        adapter.onShareClick = { Data ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${Data.videoplayUrl}"
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
        super.onDestroyView()
        _binding = null
    }
}