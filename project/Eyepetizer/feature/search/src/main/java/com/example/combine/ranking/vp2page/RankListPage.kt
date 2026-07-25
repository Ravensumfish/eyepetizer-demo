/**
 * description: 排行榜页面
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/19
 */

package com.example.combine.ranking.vp2page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.combine.ranking.RankViewModel
import com.example.combine.ranking.adapter.RankRvAdapter
import com.example.search.databinding.PageRankListBinding
import com.therouter.TheRouter

class RankListPage: Fragment() {

    lateinit var binding: PageRankListBinding
    private val rankAdapter = RankRvAdapter()
    private val viewModel: RankViewModel by activityViewModels()
    private var index :Int = 0

    //用于创建page fragment实例
    //由于排行榜三个页面重复率太高(一模一样)，统一使用RankListPage创建实例
    //静态类实现无对象的方法调用，实现带参构造与参数传递
    companion object{
        private const val INDEX = "tab_index"
        fun newInstance(tabIndex:Int): RankListPage{
            return RankListPage().apply {
                arguments = Bundle().apply {
                    putInt(INDEX,tabIndex)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageRankListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initData()
        toVideoDetail()
    }

    fun init(){
        //拿到实例创建时对应的索引
        index = arguments?.getInt(INDEX)?:0
        binding.rvRankList.adapter = rankAdapter
        binding.rvRankList.layoutManager = LinearLayoutManager(requireContext())
    }

    fun initData(){
        loadData(index)
    }

    fun loadData(index:Int){
        //使用对应索引判断应该加载什么数据
       when(index){
            0->{
                viewModel.weeklyList.observe(viewLifecycleOwner){l->
                    rankAdapter.submitList(l)
                }
                viewModel.loadWeeklyList()
            }
            1->{
                viewModel.monthlyList.observe(viewLifecycleOwner){l->
                    rankAdapter.submitList(l)
                }
                viewModel.loadMonthlyList()
            }
            2->{
                viewModel.historicalList.observe(viewLifecycleOwner){l->
                    rankAdapter.submitList(l)
                }
                viewModel.loadHistoricalList()
            }
            else -> rankAdapter.submitList(emptyList())
        }
    }

    fun toVideoDetail(){
        rankAdapter.onItemClick ={pos,item->
            Log.d("TAG", "RankList:toVideoDetail:点击了$pos,正在执行跳转 ")
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
}