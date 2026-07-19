/**
 * description: 基本的Rv适配器
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/15
 */

package com.example.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//传入数据类型，rv布局 R.layout.xxx
abstract  class BaseRvAdapter<T>()
    : RecyclerView.Adapter<BaseRvAdapter<T>.BaseRvViewHolder>(){

    protected val data = mutableListOf<T>()

    //用于提交更新后的rv列表
    //使用 submitList 传入数据列表才能刷新rv
    fun submitList(list : List<T>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    //这里子类设置布局文件
    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRvViewHolder

    //子类去实现具体绑定逻辑
    //如
    //val item = data[position]
    //holder.content.text = item.content (对应控件id)

    abstract override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int)

    override fun getItemCount(): Int {
        return data.size
    }

    var onItemClick : ((position : Int, item:T)-> Unit)? = null
    //子类去实现具体绑定逻辑
    open inner class BaseRvViewHolder(item: View): RecyclerView.ViewHolder(item){

        init {
            item.setOnClickListener {
                val pos = adapterPosition
                Log.d("TAG", "baseAdapter:点击事件，pos=$pos")
                if (pos != RecyclerView.NO_POSITION && data[pos]!= null){
                    onItemClick?.invoke(pos, data[pos])
                }
            }
        }
    }
    /*如
    val content : TextView = item.findViewById(R.id.xxx)
    //...
    * */

}