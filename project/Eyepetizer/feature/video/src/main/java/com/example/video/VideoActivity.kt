package com.example.video

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.video.adapter.VideoVp2Adapter
import com.example.video.databinding.ActivityVideoLayoutBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.shuyu.gsyvideoplayer.GSYVideoManager

class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoLayoutBinding
    lateinit var pageAdapter : VideoVp2Adapter
    private val viewModel : VideoViewModel by viewModels()
    private var id :Int = 277859

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    fun init(){
        viewModel.setVideoId(id)
        initVp2()
        binding.gsyVideoPlayer.apply {
            setUp(viewModel.loadBrief().playUrl,true,"")
            startPlayLogic()
        }

        binding.tvVideoAuthorName.text = viewModel.loadBrief().author.name
        Glide.with(this)
            .load(viewModel.loadBrief().author.icon)
            .into(binding.imgVideoAuthorAvatar)

    }

    fun initVp2(){
        pageAdapter = VideoVp2Adapter(this)
        binding.vp2Video.adapter = pageAdapter
        TabLayoutMediator(binding.tabVideo,binding.vp2Video){
            tab,pos->
            tab.text = when(pos){
                0->"简介"
                1->"评论"
                else -> ""
            }
        }.attach()
    }

    override fun onPause() {
        super.onPause()
        binding.gsyVideoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        binding.gsyVideoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}

