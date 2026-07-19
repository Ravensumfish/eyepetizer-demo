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
import com.example.combine.ranking.RankListFragment
import com.example.data.store.SPUtils
import com.example.combine.search.LabelClickCallBack
import com.example.combine.search.RankClickCallBack
import com.example.search.R
import com.example.combine.search.SearchRecordFragment
import com.example.combine.search.SearchResultFragment
import com.example.combine.search.SearchViewModel
import com.example.search.databinding.ActivityCombinedBinding


class CombinedActivity : AppCompatActivity() {

    lateinit var binding: ActivityCombinedBinding
    private val viewModel: SearchViewModel by viewModels()

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

    }

    fun initFm(){
        recordFragment = SearchRecordFragment()
        resultFragment = SearchResultFragment()
        rankFragment = RankListFragment()
        fragmentManager = supportFragmentManager
        showFragment(recordFragment)

    }

    fun init(){
        SPUtils.init(this)
        initFm()
    }

    fun showFragment(f: Fragment){
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view,f)
            .commit()
    }

    fun toSearch(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                showFragment(recordFragment)
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0!=null) {
                    viewModel.setQuery(p0)
                    showFragment(resultFragment)
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
               binding.searchView.visibility = View.GONE
               showFragment(rankFragment)
           }
       })
    }

    fun addRecord(s :String){
        val set = SPUtils.getStringSet("record")
        recordList = set.toMutableList()
        recordList.add(s)
        SPUtils.putStringSet("record",recordList)
        viewModel.loadRecord()
    }

    fun getQueryFromFragment(){
        recordFragment.setLabelClickCallBack(object : LabelClickCallBack {
            override fun getQueryFromLabel(s: String) {
                binding.searchView.setQuery(s,true)
            }
        })
    }





}