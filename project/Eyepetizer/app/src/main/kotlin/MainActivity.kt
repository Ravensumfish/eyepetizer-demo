package com.example.eyepetizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.example.data.store.SPUtils

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
        ARouter.getInstance()
            .build("/feature/home/MainActivity")
            .navigation()


    }
}
