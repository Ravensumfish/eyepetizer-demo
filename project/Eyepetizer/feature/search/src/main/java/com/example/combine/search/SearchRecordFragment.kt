/**
 * description: 搜索页面
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */

package com.example.combine.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.store.SPUtils
import com.example.combine.search.adapter.SearchLabelRvAdapter
import com.example.combine.search.adapter.RankPreviewAdapter
import com.example.search.databinding.FragmentSearchRecordBinding
import com.therouter.TheRouter

class SearchRecordFragment: Fragment() {

    private var _binding: FragmentSearchRecordBinding? = null
    private val binding get() = _binding!!
    //声明为var且可空，防止引用一直持有无法被自动回收
    private var recordAdapter : SearchLabelRvAdapter? = null
    private  var recommendAdapter : SearchLabelRvAdapter? = null
    private var rankAdapter : RankPreviewAdapter? = null
    private val viewModel : SearchViewModel by activityViewModels()

    private var labelClickCallBack : LabelClickCallBack? =null
    private var rankClickCallBack : RankClickCallBack? =null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRecordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "OnViewCreated: searchRecordFragment开始初始化")
        init()
        initData()
        initEvent()

    }

    fun initData(){
        loadRecommendLabels()
        loadRecordLabels()
        loadWeeklyRank()
        Log.d("TAG", "initData: searchRecordFragment所有列表加载完毕")
    }

    fun initEvent(){
        clickItem()
        clickRankPreview()
        clickDeleteRecord()
    }

    fun loadRecommendLabels(){
        viewModel.recommendList.observe(viewLifecycleOwner){ list->
            recommendAdapter?.submitList(list)
        }

        viewModel.loadRecommend()
    }

    fun loadRecordLabels(){
        viewModel.recordList.observe(viewLifecycleOwner){
            list->
            if (list.isEmpty()){
                binding.searchRecord.visibility = View.GONE
            }else{
                binding.searchRecord.visibility = View.VISIBLE
            }

            recordAdapter?.submitList(list)
        }
        viewModel.loadRecord()

    }

    fun loadWeeklyRank(){
        viewModel.weeklyRankList.observe(viewLifecycleOwner){
            l->
            rankAdapter?.submitList(l)
        }
        viewModel.loadWeeklyRankPreview()
    }

    fun init(){
        initRv()
    }

    fun initRv(){
        recordAdapter = SearchLabelRvAdapter()
        recommendAdapter = SearchLabelRvAdapter()
        rankAdapter = RankPreviewAdapter()
        binding.rvRecord.adapter = recordAdapter
        binding.rvRecommend.adapter = recommendAdapter
        binding.rvRk.adapter = rankAdapter
        //requireContext()可获取宿主activity的context
        binding.rvRecord.layoutManager = GridLayoutManager(requireContext(),3)
        binding.rvRecommend.layoutManager = GridLayoutManager(requireContext(),3)
        binding.rvRk.layoutManager = LinearLayoutManager(requireContext())
    }

    fun clickItem(){

        recordAdapter?.onItemClick = onItemClick@{ pos,item ->
            labelClickCallBack?.getQueryFromLabel(item)
            Log.d("TAG", "clickItem: 点击record$item")
        }

        recommendAdapter?.onItemClick = onItemClick@{ pos,item ->
            labelClickCallBack?.getQueryFromLabel(item)
            Log.d("TAG", "clickItem: 点击recommend$item")
        }

    }

    fun setLabelClickCallBack(callBack: LabelClickCallBack){
        labelClickCallBack = callBack
    }

    fun setRankClickCallBack(callBack: RankClickCallBack){
        rankClickCallBack = callBack
    }

    fun clickDeleteRecord(){
        binding.tvSearchRecordDelete.setOnClickListener {
            recordAdapter?.submitList(emptyList())
            val account = SPUtils.getString("last_account")
            SPUtils.putStringSet("record_$account",emptyList())
        }
    }

    fun clickRankPreview(){
        binding.cdSearchRk.setOnClickListener {
            rankClickCallBack?.rankPreviewClick()
        }
        rankAdapter?.onItemClick = {pos,item->
            Log.d("TAG", "RecordFragment:toVideoDetail:点击了$pos,正在执行跳转 ")
            TheRouter
                .build("/feature/video/VideoActivity")
                .withInt("id",item.id)
                .withString("title",item.title)
                .withString("icon",item.author.icon)
                .withString("name",item.author.name)
                .withString("category",item.category)
                .withString("description",item.description)
                .withString("playUrl",item.playUrl)
                .withInt("collectionCount",item.consumption.collectionCount)
                .withInt("shareCount",item.consumption.shareCount)
                .withInt("replyCount",item.consumption.replyCount)
                .navigation()

        }
    }

    //处理内存泄漏：rv未正常回收
    override fun onDestroyView() {
        //先清空adapter对rv的引用
        binding.rvRecord.adapter = null
        binding.rvRecommend.adapter = null
        binding.rvRk.adapter = null
        //置空adapter
        recordAdapter = null
        rankAdapter = null
        recommendAdapter = null
        _binding = null
        super.onDestroyView()

    }


}