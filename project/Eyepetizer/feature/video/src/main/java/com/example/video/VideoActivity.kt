package com.example.video

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
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
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = "/feature/video/VideoActivity")
class VideoActivity : AppCompatActivity(), VideoBriefFragment.VideoClickCallBack, VideoBriefFragment.CommentClickCallBack {
    private var _binding: ActivityVideoLayoutBinding? = null
    private val binding get() = _binding!!
    private var pageAdapter : VideoVp2Adapter? = null
    private val viewModel : VideoViewModel by viewModels()
    @Autowired
     var id :Int = 0
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

    lateinit var orientationUtils : OrientationUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVideoLayoutBinding.inflate(layoutInflater)
        //通过TheRouter与注解拿到所需数据
        TheRouter.inject(this)
        setContentView(binding.root)
        init()
        initPlayer()
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
        GSYVideoManager.releaseAllVideos()
        if (orientationUtils!=null){
            orientationUtils.releaseListener()
        }
        binding.vp2Video.adapter = null
        binding.gsyVideoPlayer.release()
        pageAdapter = null
        _binding = null

        super.onDestroy()

    }

    fun initPlayer(){
        fit()
        //videoCallBack()
        //设置旋转工具
        orientationUtils = OrientationUtils(this,binding.gsyVideoPlayer)
        fullScreen()
        onPlayerBackClick()
    }

    fun init(){

        Log.d("TAG", "videoActivity:跳转成功 ")
        Log.d("TAG", "videoActivity 目前视频的id: $id")
        viewModel.setVideoId(id)
        initVp2()
        viewModel.init(getBrief())
        refreshBrief()
    }

    fun fullScreen(){
        binding.gsyVideoPlayer.fullscreenButton.setOnClickListener {

            if (orientationUtils.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                showView()
                orientationUtils.resolveByClick()
            }else{
                orientationUtils.resolveByClick()
                hideView()
                //隐藏actionBar 隐藏statusBar
                binding.gsyVideoPlayer.startWindowFullscreen(this,true,true)
            }

        }

        binding.gsyVideoPlayer.setVideoAllCallBack(null)
    }

    fun onPlayerBackClick(){
        binding.gsyVideoPlayer.backButton.setOnClickListener {
            //横屏状态切换竖屏
            if (orientationUtils.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
              binding.gsyVideoPlayer.fullscreenButton.performClick()
            }
            showView()

        }

    }

    fun hideView(){
        binding.lVideoTop.visibility = View.GONE
        binding.tabVideo.visibility = View.GONE
        binding.vp2Video.visibility = View.GONE
    }

    fun showView(){
        binding.lVideoTop.visibility = View.VISIBLE
        binding.tabVideo.visibility = View.VISIBLE
        binding.vp2Video.visibility = View.VISIBLE
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

    override fun toComment() {
        Log.d("TAG", "toComment:点击评论 ")
        binding.vp2Video.currentItem = 1
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

    fun fit(){
        window.setBackgroundDrawableResource(R.color.black)
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P){
            val lp : WindowManager.LayoutParams = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }

        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT)
    }

    fun videoCallBack(){
        binding.gsyVideoPlayer.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                super.onEnterFullscreen(url, *objects)
                hideView()

            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
                showView()

            }
        })
    }



}

