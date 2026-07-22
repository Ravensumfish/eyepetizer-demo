package com.example.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.adapter.CategoryDetailAdapter
import com.example.home.databinding.FragmentCategorydetailBinding
import com.example.home.viewmodel.CategoryViewModel
import com.example.home.discoverymodel.Data
import com.bumptech.glide.Glide
import com.example.home.discoverymodel.DiscoveryCategoryDataItem


class CategoryDetailFragment: Fragment() {
    private var mId=0
    private var mName=""
    private var mHeaderImage=""
    private var mDescription=""
    private var mBgpicture=""

    private var _binding: FragmentCategorydetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var vm: CategoryViewModel
    private val adapter = CategoryDetailAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategorydetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPicture
        binding.tvDescription
        binding.tvSecondcategory
        binding.rvCategory.layoutManager = LinearLayoutManager(context)
        binding.rvCategory.adapter = adapter
        binding.rvCategory.isNestedScrollingEnabled=false
        binding.swipeRefresh.setDistanceToTriggerSync(140)

        arguments?.let {
            mId=it.getInt("id")
            mName=it.getString("name","")
            mHeaderImage=it.getString("headerImage","")
            mDescription=it.getString("description","")
            mBgpicture=it.getString("bgPicture","")
        }
        vm = ViewModelProvider(this)[CategoryViewModel::class.java]

        vm.getCategoryVideos(mId)
        vm.getTopMessage(mId)

        // 拿到首页头部数据
        fun setTopInfo(topData: DiscoveryCategoryDataItem) {
            // 顶部背景图
            Glide.with(this)
                .load(topData.headerImage)
                .into(binding.ivPicture)
            // 分类名
            binding.tvSecondcategory.text = topData.name
            // 简介
            binding.tvDescription.text = topData.description
        }

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
