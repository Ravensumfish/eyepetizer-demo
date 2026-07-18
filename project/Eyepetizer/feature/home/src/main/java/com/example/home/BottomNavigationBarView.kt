package com.example.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.home.databinding.ViewBottomNavigationbarBinding

class BottomNavigationBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewBottomNavigationbarBinding

    var onSelectListener: OnSelectListener? = null

    init {

        binding = ViewBottomNavigationbarBinding.inflate(LayoutInflater.from(context), this, true)

        //设置点击事件
        setOnClickListener(
            binding.flHome,
            binding.flDaily,
            binding.flFind,
            binding.flMine
        ) { clickedFl ->
            resetBtnState()

            when (clickedFl) {
                binding.flHome -> {
                    binding.ivHome.visibility = VISIBLE
                    binding.tvHome.visibility = GONE
                    onSelectListener?.onSelected(0)
                }
                binding.flDaily -> {
                    binding.ivDaily.visibility = VISIBLE
                    binding.tvDaily.visibility = GONE
                    onSelectListener?.onSelected(1)
                }

                binding.flFind -> {
                    binding.ivFind.visibility = VISIBLE
                    binding.tvFind.visibility = GONE
                    onSelectListener?.onSelected(2)
                }
                binding.flMine -> {
                    binding.ivMine.visibility = VISIBLE
                    binding.tvMine.visibility = GONE
                    onSelectListener?.onSelected(3)
                }
            }
        }
    }

    //重置所有按钮选中状态
    private fun resetBtnState() {
        binding.ivHome.visibility = GONE
        binding.tvHome.visibility = VISIBLE
        binding.ivDaily.visibility = GONE
        binding.tvDaily.visibility = VISIBLE
        binding.ivFind.visibility = GONE
        binding.tvFind.visibility = VISIBLE
        binding.ivMine.visibility = GONE
        binding.tvMine.visibility = VISIBLE
    }

    //默认选中首页
    fun setDefaultPage() {
        binding.flHome.performClick()
    }

    private fun setOnClickListener(vararg v: View?, block: (View) -> Unit) {
        v.forEach { it?.setOnClickListener(block) }
    }

    interface OnSelectListener {
        fun onSelected(pos: Int)
    }

    fun setSelectListener(block: (Int) -> Unit) {
        this.onSelectListener = object : OnSelectListener {
            override fun onSelected(pos: Int) {
                block(pos)
            }
        }
    }


}