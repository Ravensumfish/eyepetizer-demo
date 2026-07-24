/**
 * description: 总activity,管理fragment
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/16
 */

package com.example.combine

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.combine.ranking.BackClickCallBack
import com.example.combine.ranking.RankListFragment
import com.example.data.store.SPUtils
import com.example.combine.search.LabelClickCallBack
import com.example.combine.search.RankClickCallBack
import com.example.search.R
import com.example.combine.search.SearchRecordFragment
import com.example.combine.search.ResultVideoPage
import com.example.combine.search.SearchResultFragment
import com.example.combine.search.SearchViewModel
import com.example.search.databinding.ActivityCombinedBinding

@Route(path = "/feature/search/CombinedActivity")
class CombinedActivity : AppCompatActivity() {

    lateinit var binding: ActivityCombinedBinding
    private val searchViewModel: SearchViewModel by viewModels()
    lateinit var recordFragment: SearchRecordFragment
    lateinit var resultFragment: SearchResultFragment
    lateinit var rankFragment : RankListFragment
    lateinit var fragmentManager : FragmentManager

    private var recordList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCombinedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TAG", "onCreate: 初始化开始")
        init()
        initEvent()

    }

    fun initEvent(){
        toSearch()
        toRanking()
        backToRecord()
        quit()

    }

    fun initFm(){
        recordFragment = SearchRecordFragment()
        resultFragment = SearchResultFragment()
        rankFragment = RankListFragment()
        fragmentManager = supportFragmentManager
        showFragment(recordFragment,"record")

    }

    fun init(){
        SPUtils.init(this)
        initFm()
    }

    fun showFragment(f: Fragment,name:String){
        fragmentManager.commit {
            //跳转前将目标同名页面一并跳出
            fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view,f)
            addToBackStack(name)
        }
    }

    fun toSearch(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                showFragment(recordFragment,"record")
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0!=null) {
                    searchViewModel.setQuery(p0)
                    showFragment(resultFragment,"result")
                    addRecord(p0)
                }
                return false
            }
        })
        getQueryFromFragment()
    }

    fun toRanking(){
       recordFragment.setRankClickCallBack(object : RankClickCallBack {
           override fun rankPreviewClick() {
               Log.d("TAG", "rankPreviewClick: 点击了周排行预览！")
               binding.searchBar.visibility = View.GONE
               showFragment(rankFragment,"ranking")
           }
       })
    }

    fun backToRecord(){
        rankFragment.setBackClickCallBack(object : BackClickCallBack {
            override fun clickArrowBack() {
                binding.searchBar.visibility = View.VISIBLE
                showFragment(recordFragment,"record")
            }
        })
    }

    fun addRecord(s :String){
        val set = SPUtils.getStringSet("record")
        recordList = set.toMutableList()
        recordList.add(s)
        SPUtils.putStringSet("record",recordList)
        searchViewModel.loadRecord()
    }

    fun getQueryFromFragment(){
        recordFragment.setLabelClickCallBack(object : LabelClickCallBack {
            override fun getQueryFromLabel(s: String) {
                binding.searchView.setQuery(s,true)
            }
        })
    }

    fun quit(){
        binding.tvSearchQuit.setOnClickListener {
            finish()
        }
    }

}