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
import com.therouter.router.Route

@Route(path = "/feature/search/CombinedActivity")
class CombinedActivity : AppCompatActivity() {

    private var _binding: ActivityCombinedBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private var recordFragment: SearchRecordFragment? =null
    private var resultFragment: SearchResultFragment? =null
    private var rankFragment : RankListFragment? =null
    lateinit var fragmentManager : FragmentManager

    private var recordList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCombinedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TAG", "onCreate: 初始化开始")
        init()
        initEvent()

    }

    fun initEvent(){
        toSearch()
        toRanking()
        backToRecord()
        listener()
        quit()

    }

    fun initFm(){
        recordFragment = SearchRecordFragment()
        resultFragment = SearchResultFragment()
        rankFragment = RankListFragment()
        fragmentManager = supportFragmentManager
        recordFragment?.let { showFragment(it,"record") }

    }

    fun init(){
        initFm()
    }

    fun showFragment(f: Fragment,name:String){
        val current = fragmentManager.findFragmentById(R.id.fragment_container_view)
        Log.d("TAG", "showFragment: 目前:$current")

        fragmentManager.commit {

                //跳转前将目标同名页面一并跳出
              //  fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
               // setReorderingAllowed(true)
                replace(R.id.fragment_container_view,f,name)
               // addToBackStack(name)
        }
    }

    fun toSearch(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                recordFragment?.let { showFragment(it,"record") }
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0!=null) {
                    searchViewModel.setQuery(p0)
                    resultFragment?.let { showFragment(it,"result") }
                    addRecord(p0)
                }
                return false
            }
        })
        getQueryFromFragment()
    }

    fun toRanking(){
       recordFragment?.setRankClickCallBack(object : RankClickCallBack {
           override fun rankPreviewClick() {
               Log.d("TAG", "rankPreviewClick: 点击了周排行预览！")
               binding.searchBar.visibility = View.GONE
               rankFragment?.let { showFragment(it,"ranking") }
           }
       })
    }

    fun backToRecord(){
        rankFragment?.setBackClickCallBack(object : BackClickCallBack {
            override fun clickArrowBack() {
                binding.searchBar.visibility = View.VISIBLE
                recordFragment?.let { showFragment(it,"record") }
            }
        })
    }

    fun addRecord(s :String){
        val account = SPUtils.getString("last_account")
        val set = SPUtils.getStringSet("record_$account")
        recordList = set.toMutableList()
        recordList.add(s)
        SPUtils.putStringSet("record_$account",recordList)
        searchViewModel.loadRecord()
    }

    fun getQueryFromFragment(){
        recordFragment?.setLabelClickCallBack(object : LabelClickCallBack {
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

    fun listener(){
        fragmentManager.addOnBackStackChangedListener {
            val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container_view)
            binding.searchBar.visibility = when (currentFragment) {
                is RankListFragment -> View.GONE
                else -> View.VISIBLE
            }
        }
    }


    override fun onDestroy() {

        recordFragment = null
        rankFragment = null
        resultFragment = null
        _binding = null
        super.onDestroy()
    }

}