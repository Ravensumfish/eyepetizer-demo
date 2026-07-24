package com.example.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


interface FragmentInterface {
    fun back(): Fragment
}
class DiscoveryPlayListAdapter(fragmentActivity: FragmentActivity,
     private val fragments: ArrayList<FragmentInterface>)
: FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return fragments[position].back()
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}
