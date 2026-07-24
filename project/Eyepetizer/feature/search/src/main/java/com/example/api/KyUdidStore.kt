package com.example.api

import android.util.Log
import com.example.data.store.SPUtils
import java.security.SecureRandom

object KyUdidStore{
    fun get():String{
        SPUtils.getString("ky_udid")?.let {
            Log.d("TAG", "get: 已经有udid了：$it")
            return it }


        val bytes = ByteArray(20)
        SecureRandom().nextBytes(bytes)
        val value = bytes.joinToString(""){
            "%02x".format(it.toInt() and 0xff)
        }

        Log.d("TAG", "get: 创建了udid：$value")
        SPUtils.putString("ky_udid",value)
        return value
    }
}