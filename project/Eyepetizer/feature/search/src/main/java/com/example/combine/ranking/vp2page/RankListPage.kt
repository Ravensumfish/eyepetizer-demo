package com.example.combine.ranking.vp2page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.combine.ranking.RankViewModel
import com.example.combine.ranking.adapter.RankRvAdapter
import com.example.search.databinding.PageRankListBinding

class RankListPage: Fragment() {

    lateinit var binding: PageRankListBinding
    private val rankAdapter = RankRvAdapter()
    private val viewModel: RankViewModel by activityViewModels()
    private var index :Int = 0

    //用于创建page fragment实例
    //由于排行榜三个页面重复率太高(一模一样)，统一使用RankListPage创建实例
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
}