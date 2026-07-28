package com.example.home.discovery.category.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.databinding.FragmentCategoryDetailBinding
import com.example.home.discovery.category.CategoryDetailAdapter
import com.example.home.discovery.category.model.DiscoveryCategoryDataItem
import com.example.home.discovery.category.viewmodel.CategoryViewModel
import com.therouter.TheRouter

class CategoryDetailFragment: Fragment() {
    private var mId=0

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var vm: CategoryViewModel
    private val videoAdapter = CategoryDetailAdapter()
    private val headerAdapter = CategoryHeaderAdapter()


    //适配RV，防止加载更多时卡顿
    val concatAdapter = ConcatAdapter(headerAdapter, videoAdapter)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
            setHasFixedSize(false)
            itemAnimator = null
        }
        binding.swipeRefresh.setDistanceToTriggerSync(140)

        arguments?.let { mId=it.getInt("id") }

        vm = ViewModelProvider(this)[CategoryViewModel::class.java]

        vm.setCategoryId(mId)

        vm.getCategoryVideos(mId)

        vm.getTopMessage(mId)

        vm.topMessage.observe(viewLifecycleOwner) { categoryList ->
            val targetData = categoryList.firstOrNull { it.id == mId }

            targetData?.let {
                headerAdapter.submitList(
                    listOf(
                        DiscoveryCategoryDataItem(
                            headerImage = it.headerImage,
                            name = it.name,
                            description = it.description
                        )
                    )
                )

                binding.tvCategory.text = it.name
            }
        }


        // 下拉刷新
        binding.swipeRefresh.setOnRefreshListener {
            vm.refresh()
        }

        //返回键
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        // 上拉加载
        videoAdapter.onLoadMore = {
            vm.loadMore()
        }


        videoAdapter.onVideoClick={ Data->
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

        videoAdapter.onShareClick = { Data ->
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
            videoAdapter.submitList(it)

        }

        // 监听刷新状态
        vm.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

       //加载更多
        vm.isLoadMore.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) {
                videoAdapter.setLoadingMore(false)
            }
        }
    }

    override fun onDestroyView() {
        binding.rvCategory.adapter=null
        _binding = null
        super.onDestroyView()
    }
}