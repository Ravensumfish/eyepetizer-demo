package com.example.video

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.video.adapter.VideoVp2Adapter
import com.example.video.databinding.ActivityVideoLayoutBinding
import com.example.video.model.Author
import com.example.video.model.Consumption
import com.example.video.model.Cover
import com.example.video.model.RelatedItem
import com.example.video.model.Tag
import com.google.android.material.tabs.TabLayoutMediator
import com.shuyu.gsyvideoplayer.GSYVideoManager

class VideoActivity : AppCompatActivity(), VideoBriefFragment.VideoClickCallBack {
    lateinit var binding: ActivityVideoLayoutBinding
    lateinit var pageAdapter : VideoVp2Adapter
    private val viewModel : VideoViewModel by viewModels()
    private var id :Int = 277859

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (savedInstanceState == null){
//            val vid = intent.getIntExtra("video_id",277859)
//            val title = intent.getStringExtra("video_title")?:""
//            val description = intent.getStringExtra("video_description")
//            val playUrl = intent.getStringExtra("video_url")!!
//            val tags = intent.getStringExtra("tags")
//            val likes = intent.getIntExtra("video_likes",0)
//            val comments = intent.getIntExtra("video_comments",0)
//            val shares = intent.getIntExtra("video_shares",0)
//            //author
//            val id = intent.getIntExtra("author_id",0)
//            val name = intent.getStringExtra("author_name")?:"未知"
//            val icon = intent.getStringExtra("author_avatar")?:""
//
//            val a = Author(id,icon,name)
//            val c = Consumption(likes,shares,comments)
//
//            val tl : List<Tag>? = tags?.split(" ")
//                ?.map { tag-> Tag(tag) }?.toList()
//
//            val brief = RelatedItem(
//                title,"",vid,a,
//                0, Cover(""),description,
//                playUrl,c,tl)
//
//            this.id = vid
//            viewModel.init(brief)
//        }


        init()

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

    fun init(){
        Log.d("TAG", "videoActivity 目前视频的id: $id")
        viewModel.setVideoId(id)
        initVp2()
        refreshBrief()
    }

    fun refreshBrief(){
        viewModel.currentBrief.observe(this){
            b->
            b?.let {
                binding.gsyVideoPlayer.apply {
                    setUp(b.playUrl,true,"")
                    startPlayLogic()
                }

                binding.tvVideoAuthorName.text = b.author?.name
                Glide.with(this)
                    .load(b.author?.icon)
                    .into(binding.imgVideoAuthorAvatar)
            }
        }
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


    override fun onVideoClick(videoId: Int) {
        Log.d("TAG", "onVideoClick: activity接收到了id:$videoId")
        viewModel.setVideoId(videoId)
        viewModel.toVideo(videoId)
        id = videoId

    }

}

