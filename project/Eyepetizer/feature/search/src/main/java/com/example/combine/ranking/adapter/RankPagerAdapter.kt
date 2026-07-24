/**
 * description: vp2适配器
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/19
 */

package com.example.combine.ranking.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.combine.ranking.vp2page.RankListPage


class RankPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabTitles = arrayOf(
        "周排行",
        "月排行",
        "总排行"
    )


    override fun createFragment(position: Int): Fragment {
        return RankListPage.newInstance(position)
    }

    override fun getItemCount(): Int = 3

    fun getTabTitles(pos:Int):String{
        return tabTitles[pos]
    }

}