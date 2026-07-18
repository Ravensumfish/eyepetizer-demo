package com.example.data.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SPUtils {
    private val SP_NAME = "prefs"

    private var appContext : Context? = null
    private val sp: SharedPreferences by lazy{
        if (appContext==null){
            throw IllegalStateException("sp未初始化")
        }
        appContext!!.getSharedPreferences(
            SP_NAME, Context.MODE_PRIVATE
        )
    }

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun putString(key: String,value:String){
        sp.edit{
            putString(key,value)
        }
    }

    fun getString(key : String) : String{
        val df = "null"
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


}