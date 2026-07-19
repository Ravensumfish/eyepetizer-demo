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
    private val fragments = arrayOf(
        //创建fragment实例作为item
        RankListPage.newInstance(0),
        RankListPage.newInstance(1),
        RankListPage.newInstance(2)
    )

    private val tabTitles = arrayOf(
        "周排行",
        "月排行",
        "总排行"
    )


    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    fun getTabTitles(pos:Int):String{
        return tabTitles[pos]
    }
}