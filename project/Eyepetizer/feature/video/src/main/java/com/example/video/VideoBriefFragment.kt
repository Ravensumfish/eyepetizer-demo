package com.example.video

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.video.adapter.BriefRvAdapter
import com.example.video.databinding.FragmentVideoBriefBinding

class VideoBriefFragment : Fragment(){
    lateinit var binding : FragmentVideoBriefBinding
    private var adapter = BriefRvAdapter()
    private val viewModel : VideoViewModel by activityViewModels()
    private var videoClickCallBack : VideoClickCallBack? = null

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
    }

    fun init(){
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
            }
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
        adapter.onItemClick = onItemClick@{ pos,item ->
            Log.d("TAG", "clickItem: 点击video${item.id}")
            videoClickCallBack?.onVideoClick(item.id)

        }
    }

    interface VideoClickCallBack {
        fun onVideoClick(videoId:Int)
    }


}