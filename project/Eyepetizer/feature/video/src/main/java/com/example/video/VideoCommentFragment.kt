package com.example.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.video.adapter.CommentRvAdapter
import com.example.video.databinding.FragmentVideoCommentsBinding

class VideoCommentFragment: Fragment() {
    lateinit var binding: FragmentVideoCommentsBinding
    private val viewModel : VideoViewModel by activityViewModels()
    private val adapter= CommentRvAdapter()
    private var hot = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoCommentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initData()
        changeOrder()
    }

    fun init(){
        binding.rvVideoComments.adapter = adapter
        binding.rvVideoComments.layoutManager = LinearLayoutManager(requireContext())
    }

    fun initData(){
        viewModel.commentList.observe(viewLifecycleOwner){
            l->
            adapter.submitList(l)
        }
        viewModel.loadComments()
        viewModel.sortBy(true)
    }
    fun changeOrder(){
        binding.commentsOrder.setOnClickListener {
            hot = !hot

            if (hot){
                binding.tvCommentsOrder.text = "按热度"
                binding.tvCommentsType.text = "最热评论"
            }else{
                binding.tvCommentsOrder.text = "按时间"
                binding.tvCommentsType.text = "最新评论"
            }

            viewModel.sortBy(hot)

        }
    }
}