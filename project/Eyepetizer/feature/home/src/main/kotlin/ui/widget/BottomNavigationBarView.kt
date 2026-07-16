package ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.home.databinding.ViewBottomNavigationbarBinding

class BottomNavigationBarView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
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
                    binding.ivHome.visibility = View.VISIBLE
                    binding.tvHome.visibility = View.GONE
                    onSelectListener?.onSelected(0)
                }
                binding.flDaily -> {
                    binding.ivDaily.visibility = View.VISIBLE
                    binding.tvDaily.visibility = View.GONE
                    onSelectListener?.onSelected(1)
                }

                binding.flFind -> {
                    binding.ivFind.visibility = View.VISIBLE
                    binding.tvFind.visibility = View.GONE
                    onSelectListener?.onSelected(2)
                }
                binding.flMine -> {
                    binding.ivMine.visibility = View.VISIBLE
                    binding.tvMine.visibility = View.GONE
                    onSelectListener?.onSelected(3)
                }
            }
        }
    }

    //重置所有按钮选中状态
    private fun resetBtnState() {
        binding.ivHome.visibility = View.GONE
        binding.tvHome.visibility = View.VISIBLE
        binding.ivDaily.visibility =  View.GONE
        binding.tvDaily.visibility = View.VISIBLE
        binding.ivFind.visibility =  View.GONE
        binding.tvFind.visibility = View.VISIBLE
        binding.ivMine.visibility =  View.GONE
        binding.tvMine.visibility = View.VISIBLE
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

    fun setSelectListener(onSelectListener: OnSelectListener) {
        this.onSelectListener = onSelectListener
    }


}