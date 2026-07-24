package com.example.combine.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.combine.ranking.adapter.RankPagerAdapter
import com.example.search.databinding.FragmentRankListBinding
import com.google.android.material.tabs.TabLayoutMediator

class RankListFragment : Fragment() {
    lateinit var binding: FragmentRankListBinding
    lateinit var pagerAdapter: RankPagerAdapter

    private var backClickCallBack : BackClickCallBack? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        back()
    }

    fun init(){
        //禁用状态保存，不加会炸，原因是fragment被回收再恢复时找不到旧的fragment
        binding.vp2Rank.isSaveEnabled=false
        pagerAdapter = RankPagerAdapter(this)
        binding.vp2Rank.adapter = pagerAdapter
        //将顶部导航栏与vp2联动
        TabLayoutMediator(binding.tabRank,binding.vp2Rank){
            tab,pos->
            tab.text = pagerAdapter.getTabTitles(pos)
        }.attach()
    }

    fun back(){
        binding.imgRankBack.setOnClickListener {
            backClickCallBack?.clickArrowBack()
        }
    }

    fun setBackClickCallBack(callBack: BackClickCallBack){
        backClickCallBack = callBack
    }
}