package com.example.video

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
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

@Route(path = "/feature/video/VideoActivity")
class VideoActivity : AppCompatActivity(), VideoBriefFragment.VideoClickCallBack {
    lateinit var binding: ActivityVideoLayoutBinding
    lateinit var pageAdapter : VideoVp2Adapter
    private val viewModel : VideoViewModel by viewModels()
    @Autowired
    private var id :Int = 0
    @Autowired
    private var title : String = ""
    @Autowired
    private var icon:String=""
    @Autowired
    private var name:String=""
    @Autowired
    private var category:String=""
    @Autowired
    private var description:String=""
    @Autowired
    private var playUrl:String=""
    @Autowired
    private var collectionCount:Int=0
    @Autowired
    private var shareCount:Int=0
    @Autowired
    private var replyCount:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLayoutBinding.inflate(layoutInflater)
        ARouter.getInstance().inject(this)
        setContentView(binding.root)
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
        //通过ARouter与注解拿到所需数据

        Log.d("跳转", "videoActivity:跳转成功 ")
        Log.d("TAG", "videoActivity 目前视频的id: $id")
        viewModel.setVideoId(id)
        initVp2()
        viewModel.init(getBrief())
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

    fun getBrief(): RelatedItem{
        val a = Author(0, icon, name)
        val c= Consumption(
            collectionCount,
            shareCount,
            replyCount,
        )
        return RelatedItem(
            title,
            "videoSmallCard",
            id,
            a,
            104,
            Cover(""),
            description,
            playUrl,
            c,
            listOf(Tag(category))
        )
    }

}

