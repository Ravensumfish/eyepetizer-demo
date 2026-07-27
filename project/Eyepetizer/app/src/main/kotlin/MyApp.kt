package com.example.eyepetizer

import android.app.Application
import android.util.Log
import com.example.data.store.SPUtils
import com.therouter.TheRouter

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        //在主模块中初始化一次，任意模块可使用
        SPUtils.init(this)

        TheRouter.init(this)

        Log.d("Application", "ARouter 初始化完成")
    }
}