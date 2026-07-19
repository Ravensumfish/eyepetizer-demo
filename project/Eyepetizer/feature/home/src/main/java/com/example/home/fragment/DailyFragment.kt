package com.example.home.fragment

import androidx.fragment.app.Fragment
import com.example.home.viewmodel.DailyViewModel
import com.example.home.adapter.DailyVideoAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.databinding.FragmentDailyBinding

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater

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

        // 上拉加载
        adapter.onLoadMore = {
            vm.loadMore()
        }

        // 监听总数据列表
        vm.videoTotalList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // 监听刷新状态
        vm.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
        vm.getDailyVideos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}