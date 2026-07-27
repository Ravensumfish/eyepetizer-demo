package com.example.video

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.data.store.SPUtils
import com.example.video.adapter.VideoVp2Adapter
import com.example.video.databinding.ActivityVideoLayoutBinding
import com.example.video.model.Author
import com.example.video.model.Consumption
import com.example.video.model.Cover
import com.example.video.model.RelatedItem
import com.example.video.model.Tag
import com.google.android.material.tabs.TabLayoutMediator
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = "/feature/video/VideoActivity")
class VideoActivity : AppCompatActivity(), VideoBriefFragment.VideoClickCallBack {
    lateinit var binding: ActivityVideoLayoutBinding
    lateinit var pageAdapter : VideoVp2Adapter
    private val viewModel : VideoViewModel by viewModels()
    @Autowired
     var id :Int = 7788
    @Autowired
     var title : String = ""
    @Autowired
     var icon:String=""
    @Autowired
     var name:String=""
    @Autowired
     var category:String=""
    @Autowired
     var description:String=""
    @Autowired
     var playUrl:String=""
    @Autowired
     var collectionCount:Int=0
    @Autowired
     var shareCount:Int=0
    @Autowired
     var replyCount:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLayoutBinding.inflate(layoutInflater)
        //通过TheRouter与注解拿到所需数据
        TheRouter.inject(this)
        setContentView(binding.root)
        init()
        back()
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

        SPUtils.init(this)

        Log.d("TAG", "videoActivity:跳转成功 ")
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

    fun back(){
        binding.imgVideoBack.setOnClickListener {
            finish()
        }
    }

}

