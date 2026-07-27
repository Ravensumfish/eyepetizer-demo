package com.example.home.mine.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.store.SPUtils
import com.example.home.R
import com.example.home.mine.adapter.MyStarAdapter
import com.example.home.databinding.FragmentMyStarBinding
import com.example.home.mine.VideoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.therouter.TheRouter

class MyStarFragment : Fragment(){
    private var _binding: FragmentMyStarBinding? = null
    private val binding get() = _binding!!
    private var adapter : MyStarAdapter?=  MyStarAdapter()
    private var account :String? = null
    private var sp: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyStarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clickItem()
        clickBack()
        refresh()
    }

    override fun onDestroyView() {
        binding.rvStarList.adapter = null
        adapter = null
        _binding = null
        super.onDestroyView()
    }

    fun init(){
        sp = SPUtils.getSP()
        binding.rvStarList.adapter = adapter
        binding.rvStarList.layoutManager = LinearLayoutManager(requireContext())
        arguments?.let {
            account = it.getString("account")
        }
        adapter?.submitList(getDataItemList())
    }

    fun clickBack(){
        binding.imgStarBack.setOnClickListener {
            findNavController().popBackStack(R.id.fragment_mine,false)
        }
    }

    fun clickItem(){
        adapter?.onItemClick = {pos,item->
            TheRouter
                .build("/feature/video/VideoActivity")
                .withInt("id",item.id)
                .withString("title",item.title)
                .withString("icon",item.author?.icon)
                .withString("name",item.author?.name)
                .withString("category",item.tags?.firstOrNull()?.title)
                .withString("description",item.description)
                .withString("playUrl",item.playUrl)
                .withInt("collectionCount",item.consumption.collectionCount)
                .withInt("shareCount",item.consumption.shareCount)
                .withInt("replyCount",item.consumption.replyCount)
                .navigation()
        }
    }

    //获取收藏列表
    fun getDataItemList(): List<VideoItem>{
        val gson = Gson()
        val NAME = "${account}_VIDEO_ID_LIST"
        val json = sp?.getString(NAME, "[]")
        Log.d("TAG", "getDataItemList:访问id库:$NAME ")
        val type = object : TypeToken<List<Int>>() {}.type
        val idList: MutableList<Int> = gson.fromJson(json, type)

        val videoList = mutableListOf<VideoItem>()
        for (id in idList) {
            val json = sp?.getString("video_$id", "")
            if (!json.isNullOrEmpty()) {
                val item = gson.fromJson(json, VideoItem::class.java)
                videoList.add(item)
            }
        }
        Log.d("TAG", "getDataItemList: 获得列表$videoList")
        Log.d("TAG", "getDataItemList: 获得列表个数${videoList.size}")
        return videoList
    }

    fun refresh(){
        binding.srMyStar.setOnRefreshListener {
            adapter?.submitList(getDataItemList())
            binding.srMyStar.isRefreshing = false
        }
    }



}