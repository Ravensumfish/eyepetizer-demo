/**
 * description: 基本的Rv适配器
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/15
 */

package com.example.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//传入数据类型，rv布局 R.layout.xxx
abstract  class BaseRvAdapter<T>(private val layoutResId : Int)
    : RecyclerView.Adapter<BaseRvAdapter<T>.BaseRvViewHolder>(){

    private val data = mutableListOf<T>()

    //用于提交更新后的rv列表
    //使用 submitList 传入数据列表才能刷新rv
    fun submitList(list : List<T>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRvViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId,parent,false)
        return BaseRvViewHolder(view)

    }

    //子类去实现具体绑定逻辑
    //如
    //val item = data[position]
    //holder.content.text = item.content (对应控件id)
    abstract override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int)

    override fun getItemCount(): Int {
        return data.size
    }

    private val onItemClick : ((position : Int,item:View)-> Unit)? = null
    //子类去实现具体绑定逻辑
 open inner class BaseRvViewHolder(item: View): RecyclerView.ViewHolder(item){

        init {
            item.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onItemClick?.invoke(pos, item)
                }
            }
        }
    }
    /*如
    val content : TextView = item.findViewById(R.id.xxx)
    //...
    * */

}