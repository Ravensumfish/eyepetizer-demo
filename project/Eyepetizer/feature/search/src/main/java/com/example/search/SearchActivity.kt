package com.example.search

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.GridLayoutManager
import com.example.search.adapter.SearchLabelRvAdapter
import com.example.search.databinding.ActivitySearchBinding
import com.example.search.ui.theme.EyepetizerTheme


class SearchActivity : ComponentActivity() {

    lateinit var binding: ActivitySearchBinding

    private  val recordAdapter : SearchLabelRvAdapter = SearchLabelRvAdapter()
    private  val recommendAdapter : SearchLabelRvAdapter = SearchLabelRvAdapter()
    //private val lbAdapter : SearchLabelRvAdapter =



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    fun initRv(){
        binding.rvRecord.layoutManager = GridLayoutManager(this,3)
        binding.rvRecommend.layoutManager = GridLayoutManager(this,3)
        binding.rvRecord.adapter = recordAdapter
        binding.rvRecommend.adapter = recommendAdapter

    }

    fun initData(){
        val data = mutableListOf<String>().apply {
            repeat(10){
                add("test-> item$it")
            }
        }

        recordAdapter.submitList(data)
    }

    fun init(){
        initRv()
        initData()


    }
}

