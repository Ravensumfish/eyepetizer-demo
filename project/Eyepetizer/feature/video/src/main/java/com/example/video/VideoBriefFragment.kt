package com.example.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.store.SPUtils
import com.example.video.adapter.BriefRvAdapter
import com.example.video.databinding.FragmentVideoBriefBinding
import com.example.video.model.RelatedItem

class VideoBriefFragment : Fragment(){
    lateinit var binding : FragmentVideoBriefBinding
    private var adapter = BriefRvAdapter()
    private val viewModel : VideoViewModel by activityViewModels()
    private var videoClickCallBack : VideoClickCallBack? = null
    private var id = 0
    lateinit var brief : RelatedItem
    private var account :String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBriefBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initEvent()
    }

    fun init(){
        account = SPUtils.getString("last_account")
        initRv()
        initData()
    }

    fun initData(){
        briefData()
        rvData()
    }

    fun briefData(){

        viewModel.currentBrief.observe(viewLifecycleOwner){
            b->
            b?.let {
                binding.tvVideoTitle.text = b.title
                binding.tvVideoDescription.text = b.description
                binding.tvVideoGoodCount.text = b.consumption.collectionCount.toString()
                binding.tvVideoShareCount.text = b.consumption.shareCount.toString()
                binding.tvVideoCommentCount.text = b.consumption.replyCount.toString()
                binding.tvVideoTags.text = b.tags?.joinToString(separator = " "){it.title?:""}
                brief = b
            }
            id = viewModel.getVideoId()
            likeAndStar()
            Log.d("TAG", "briefData:id$id ")
        }


    }

    fun rvData(){
        viewModel.relatedList.observe(viewLifecycleOwner){
                l->
            adapter.submitList(l)
            binding.nestedVideoBrief.scrollTo(0,0)

        }
        viewModel.loadRelated()
    }

    fun initRv(){
        openVideoDetail()
        binding.rvVideoRelated.adapter = adapter
        binding.rvVideoRelated.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        videoClickCallBack = context as VideoClickCallBack
        Log.d("TAG", "onAttach: $context")
        Log.d("TAG", "onAttach:callback set: $videoClickCallBack")
    }

    override fun onDetach() {
        super.onDetach()
        videoClickCallBack = null
    }

    fun openVideoDetail(){
        adapter.onItemClick = { pos,item ->
            Log.d("TAG", "clickItem: 点击video${item.id}")
            videoClickCallBack?.onVideoClick(item.id)

        }
    }

    interface VideoClickCallBack {
        fun onVideoClick(videoId:Int)
    }

    fun initEvent(){
        clickLike()
        clickStar()
        clickShare()
    }

    fun clickShare(){
        binding.imgVideoShare.setOnClickListener {
            share()
        }
    }

    fun share(){
        val link = "https://m.eyepetizer.net/u1/video-detail?video_id=$id"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,"分享视频:$link")
        }
        startActivity(Intent.createChooser(shareIntent,"分享到"))
    }

    fun clickLike(){
        binding.imgVideoGood.setOnClickListener {
            if (SPUtils.getString("last_account").isNullOrEmpty()) {
                Toast.makeText(requireContext(), "还未登陆哦!", Toast.LENGTH_SHORT).show()
            } else {

                if (SPUtils.getBool("like_$id")) {
                    binding.imgVideoGood.setImageResource(R.drawable.icon_good)
                    SPUtils.putBool("like_$id", false)
                    val s = binding.tvVideoGoodCount.text.toString()
                    val count = (s.toInt() - 1).toString()
                    binding.tvVideoGoodCount.text = count
                } else {
                    binding.imgVideoGood.setImageResource(R.drawable.icon_like_solid)
                    SPUtils.putBool("like_$id", true)
                    val s = binding.tvVideoGoodCount.text.toString()
                    val count = (s.toInt() + 1).toString()
                    binding.tvVideoGoodCount.text = count
                }

            }
        }
    }

    fun clickStar(){
        binding.imgVideoStar.setOnClickListener {

            if (SPUtils.getString("last_account").isNullOrEmpty()) {
                Toast.makeText(requireContext(), "还未登陆哦!", Toast.LENGTH_SHORT).show()
            } else {
                if (SPUtils.getBool("star_${id}_$account")) {
                    binding.imgVideoStar.setImageResource(R.drawable.icon_star)
                    SPUtils.putBool("star_${id}_$account", false)
                    binding.tvVideoStar.text = "收藏"

                    SPUtils.deleteDataItem(brief, brief.id)
                    Log.d("TAG", "clickStar: 取消收藏")


                } else {
                    binding.imgVideoStar.setImageResource(R.drawable.icon_star_solid)
                    SPUtils.putBool("star_${id}_$account", true)
                    binding.tvVideoStar.text = "已收藏"

                    SPUtils.saveDataItem(brief, brief.id)
                    Log.d("TAG", "clickStar: 收藏成功")

                }
            }
        }
    }

    fun likeAndStar(){

        var like = SPUtils.getBool("like_${id}_$account")
        var star = SPUtils.getBool("star_${id}_$account")

        if (account==null){
            like = false
            star = false
        }

        Log.d("TAG", "likeAndStar: like:$like")
        Log.d("TAG", "likeAndStar: star:$star")

        val imgGood = when(like){
            true-> R.drawable.icon_like_solid
            false->R.drawable.icon_good
        }

        val imgStar = when(star){
            true-> R.drawable.icon_star_solid
            false->R.drawable.icon_star
        }

        val tvGood = when(like){
            true-> {
                val s = binding.tvVideoGoodCount.text.toString()
                val count = (s.toInt() + 1).toString()
                count
            }
            false-> {
                binding.tvVideoGoodCount.text.toString()
            }
        }

        val tvStar = when(star){
            true-> "已收藏"
            false->"收藏"
        }

        binding.imgVideoGood.setImageResource(imgGood)
        binding.imgVideoStar.setImageResource(imgStar)

        binding.tvVideoGoodCount.text = tvGood
        binding.tvVideoStar.text = tvStar
    }


}