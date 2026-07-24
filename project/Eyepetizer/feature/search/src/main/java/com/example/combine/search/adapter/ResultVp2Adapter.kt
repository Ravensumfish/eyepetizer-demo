package com.example.combine.search.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.combine.search.ResultAuthorPage
import com.example.combine.search.ResultImagePage
import com.example.combine.search.ResultTopicPage
import com.example.combine.search.ResultUserPage
import com.example.combine.search.ResultVideoPage
import kotlin.math.log

class ResultVp2Adapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun createFragment(position: Int): Fragment {
        return when(position){
                0-> ResultVideoPage()
                1-> ResultAuthorPage()
                2-> ResultImagePage()
                3-> ResultTopicPage()
                4-> ResultUserPage()
                else -> {
                    Log.d("TAG", "Search:createFragment:无 ")
                    throw IllegalArgumentException()
                }
        }
    }

    override fun getItemCount(): Int = 5

}