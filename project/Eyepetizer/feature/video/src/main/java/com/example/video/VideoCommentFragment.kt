package com.example.video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.video.adapter.CommentRvAdapter
import com.example.video.databinding.FragmentVideoCommentsBinding

class VideoCommentFragment: Fragment() {
    private var _binding: FragmentVideoCommentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : VideoViewModel by activityViewModels()
    private var adapter : CommentRvAdapter?= CommentRvAdapter()
    private var hot = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoCommentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initData()
        changeOrder()
        refresh()
        loadMore()
    }

    override fun onDestroyView() {
        binding.rvVideoComments.adapter = null
        adapter = null
        _binding = null
        super.onDestroyView()
    }


    fun init(){
        binding.rvVideoComments.adapter = adapter
        binding.rvVideoComments.layoutManager = LinearLayoutManager(requireContext())
    }

    fun initData(){
        viewModel.commentList.observe(viewLifecycleOwner){
            l->
            adapter?.submitList(l)
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

    fun refresh(){
        viewModel.isRefreshing.observe(viewLifecycleOwner){
            b->
            binding.srComment.isRefreshing = b
        }

        binding.srComment.setOnRefreshListener {
            if (binding.srComment.verticalScrollbarPosition == 0
                && viewModel.isRefreshing.value !=true){
                hot = false
                binding.tvCommentsOrder.text = "按时间"
                binding.tvCommentsType.text = "最新评论"

                viewModel.refreshComments()
            }
        }
    }

    fun loadMore(){
        if (viewModel.isLoading.value==true)return


        binding.nestedVideoComment.viewTreeObserver.addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                val scroll = binding.nestedVideoComment
                val child = scroll.getChildAt(0)

                //绝对高度>目前总高度-预加载高度时进行加载
                //向下滚动时，y++，scrolly变大，加上屏幕可见高度后为已划过高度
                if (viewModel.isLoading.value != true && scroll.scrollY + scroll.height>child.height-150){
                    Log.d("TAG", "onScrollChanged: 正在加载更多评论")
                    viewModel.loadMoreComments()
                }
            }

        })
    }
}