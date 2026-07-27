package com.example.data.store

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SPUtils {
    private val SP_NAME = "prefs"
    private val vl = "VIDEO_ID_LIST"

    private var appContext : Context? = null
    private val sp: SharedPreferences by lazy{
        if (appContext==null){
            throw IllegalStateException("sp未初始化")
        }
        appContext!!.getSharedPreferences(
            SP_NAME, Context.MODE_PRIVATE
        )
    }
    fun getSPContext() : Context?{
        return appContext
    }

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getSP() : SharedPreferences{
        return sp
    }

    //将具体数据类对象转化为json存入sp，实现轻量级存储，可用于视频点赞收藏等等
    //目前定死为视频列表，可更改传参变得更泛用
    fun <T> saveDataItem(item:T,id: Int){
        val account = getString("last_account")
        val gson = Gson()
        val itemJson = gson.toJson(item)
        sp.edit {
            putString("video_$id",itemJson)
            Log.d("TAG", "saveDataItem: 存入收藏视频:$itemJson")
        }
        //取出id数组
        val json = sp.getString("${account}_$vl","[]")
        Log.d("TAG", "saveDataItem:访问id库:${account}_$vl ")

        //指定数据类型
        val type = object : TypeToken<List<Int>>(){}.type
        //将拿到的json转化为int动态数组
        val idList : MutableList<Int> = gson.fromJson(json,type)
        if (!idList.contains(id)){
            idList.add(id)
            sp.edit{
                val idJson = gson.toJson(idList)
                putString("${account}_$vl",idJson)
                Log.d("TAG", "saveDataItem: 更新收藏视频id列表:$idJson")
            }
        }
    }

    fun <T> deleteDataItem(item:T,id: Int){
        val account = getString("last_account")
        val gson = Gson()

        //取出id数组
        val json = sp.getString("${account}_$vl","[]")
        Log.d("TAG", "deleteDataItem:访问id库:${account}_$vl ")
        //指定数据类型
        val type = object : TypeToken<List<Int>>(){}.type
        //将拿到的json转化为int动态数组
        val idList : MutableList<Int> = gson.fromJson(json,type)
        if (idList.contains(id)){
            idList.remove(id)
            sp.edit{
                putString(vl,gson.toJson(idList))
                remove("video_$id")
            }
        }
    }



    fun putString(key: String,value:String){
        sp.edit{
            putString(key,value)
        }
    }

    fun getString(key : String) : String?{
        val df = null
        return sp.getString(key,df)?:df
    }

    fun putStringSet(key : String, list : List<String>){
        val set = list.toSet()
        sp.edit{
            putStringSet(key,set)
        }
    }

    fun getStringSet(key: String) : Set<String> {
        return sp.getStringSet(key,emptySet())?:emptySet()
    }

    fun putBool(key: String,value: Boolean){
        sp.edit{
            putBoolean(key,value)
        }
    }

    fun getBool(key : String) : Boolean{

        return sp.getBoolean(key,false)
    }




}