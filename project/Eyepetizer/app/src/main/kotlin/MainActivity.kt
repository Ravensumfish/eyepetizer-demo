package com.example.eyepetizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.data.store.SPUtils
import com.therouter.TheRouter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        init()

    }

    fun init(){
        //在主模块中初始化一次，任意模块可使用
        SPUtils.init(this)
        //配置初始页面
        TheRouter.build("/feature/home/MainActivity").navigation()

    }
}
