package com.example.video.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.video.VideoActivity
import com.example.video.VideoBriefFragment
import com.example.video.VideoCommentFragment

class VideoVp2Adapter(activity: VideoActivity) : FragmentStateAdapter(activity){

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> VideoBriefFragment()
            1-> VideoCommentFragment()
            else -> {
                Log.d("TAG", "createFragment:pos越界 ")
                throw IllegalArgumentException()
            }
        }
    }



    override fun getItemCount(): Int = 2


}