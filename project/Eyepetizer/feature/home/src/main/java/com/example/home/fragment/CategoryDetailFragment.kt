package com.example.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.adapter.CategoryDetailAdapter
import com.example.home.databinding.FragmentCategorydetailBinding
import com.example.home.viewmodel.CategoryViewModel
import com.bumptech.glide.Glide


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
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("CrashHandler","崩溃",throwable)
        }
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

        vm.setCategoryId(mId)

        vm.loadCategoryVideos(mId)
        vm.getTopMessage(mId)
        vm.topMessage.observe(viewLifecycleOwner){
            categoryList ->
            val targetData=categoryList.firstOrNull{item -> item.id==mId
            }
            targetData?.let {
                Glide.with(requireContext())
                    .load(it.bgPicture)
                    .into(binding.ivPicture)
                binding.tvSecondcategory.text=it.name
                binding.tvDescription.text=it.description
            }
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
