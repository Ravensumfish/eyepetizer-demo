/**
 * description: 网络请求客户端
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */


package com.example.api

import android.util.Log
import com.example.net.utils.HttpConfig
import com.example.net.utils.RetrofitClient.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//配置全局唯一
object SearchRetrofitClient {


    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(CookieInterceptor(KyUdidStore))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            Log.d("TAG", "POST: $level")
        })
        //配置超时
        .connectTimeout(HttpConfig.TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(HttpConfig.TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(HttpConfig.TIMEOUT, TimeUnit.SECONDS)
        .build()



    private val retrofit = Retrofit.Builder()
        .baseUrl(HttpConfig.BASE_URL_EYEPETIZER)
        //Json解析,将json字符串转化为自定义数据类对象
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        //转换响应接口为rxjava类型，返回observable或flowable类型，从而可以调用rxjava方法，如线程切换
        .addCallAdapterFactory(
            RxJava3CallAdapterFactory.create()
        )
        //使用配置好的okhttp
        .client(okHttpClient)
        .build()

    fun<T> create(clazz: Class<T>):T{
        //retrofit动态代理，根据传入的接口与接口注解自动生成实现该接口的实例
        return retrofit.create(clazz)
    }
}