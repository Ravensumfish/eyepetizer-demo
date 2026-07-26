package com.example.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.home.adapter.PlayListDetailAdapter
import com.example.home.databinding.FragmentPlaylistdetailBinding
import com.example.home.viewmodel.PlayListViewModel
import com.therouter.TheRouter

class PlayListDetailFragment : Fragment(){

    private var mId=0

    private var mHeaderImage=""
    private var mDescription=""
    private var _binding: FragmentPlaylistdetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var vm: PlayListViewModel
    private val adapter = PlayListDetailAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("CrashHandler","崩溃",throwable)
        }
        binding.ivHeader
        binding.tvDes
        binding.btnBrief
        binding.rvPlaylist.layoutManager = LinearLayoutManager(context)
        binding.rvPlaylist.adapter = adapter
        binding.rvPlaylist.isNestedScrollingEnabled=false
        binding.swipeRefresh.setDistanceToTriggerSync(140)

        arguments?.let {
            mId=it.getInt("id")
            mHeaderImage=it.getString("headerImage","")
            mDescription=it.getString("description","")
        }
        vm = ViewModelProvider(this)[PlayListViewModel::class.java]

        vm.setPlayListId(mId)

        vm.getPlayListVideos(mId)


        vm.topMessage.observe(viewLifecycleOwner) { topicDetail ->
            topicDetail?.let {
                Glide.with(requireContext())
                    .load(it.headerImage)
                    .into(binding.ivHeader)
                binding.tvDes.text = it.text
                binding.btnBrief.text = it.brief
                binding.tvTitle.text=it.brief
            }
        }


        // 下拉刷新
        binding.swipeRefresh.setOnRefreshListener {
            vm.refresh()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


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

        adapter.onVideoClick={ Data->
            TheRouter
                .build("/feature/video/VideoActivity")
                .withInt("id",Data.content.data.id)
                .withString("title",Data.content.data.title)
                .withString("name",Data.content.data.author.name)
                .withString("icon",Data.content.data.author.icon)
                .withString("category",Data.content.data.category)
                .withString("description",Data.content.data.description)
                .withString("playUrl",Data.content.data.playUrl)
                .withInt("collectionCount",Data.content.data.consumption.collectionCount)
                .withInt("shareCount",Data.content.data.consumption.shareCount)
                .withInt("replyCount",Data.content.data.consumption.replyCount)
                .navigation()

        }

        // 监听总数据列表
        vm.videoTotalList.observe(viewLifecycleOwner) {
            adapter.setData(it)
            Log.e("适配", "=== submitList ===")
            Log.e("适配", "新列表是否为空: ${it == null}")
            Log.e("适配", "新列表数量: ${it?.size}")
            Log.e(
                "zhangpl",
                "观察结果: size=${it.size}, viewModel=${System.identityHashCode(vm)}"
            )


        }

        // 监听刷新状态
        vm.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
