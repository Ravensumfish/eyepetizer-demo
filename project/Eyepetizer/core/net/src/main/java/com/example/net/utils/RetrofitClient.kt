/**
 * description: 封装网络请求工具,retrofit单例管理
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/7/17
 */

package com.example.net.utils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    //配置okhttp客户端
    val okHttpClient = OkHttpClient.Builder()
        //配置超时
        .connectTimeout(HttpConfig.TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(HttpConfig.TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(HttpConfig.TIMEOUT, TimeUnit.SECONDS)
        .build()

    //创建retrofit对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(HttpConfig.BASE_URL)
        //Json解析,将json字符串转化为自定义数据类对象
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        //转换响应接口为rxjava类型从而可以调用rxjava方法
        .addCallAdapterFactory(
            RxJava3CallAdapterFactory.create()
        )
        //使用配置好的okhttp
        .client(okHttpClient)
        .build()

        //创建接口实例
        //如： private val api: SearchAPIService= RetrofitClient.create(SearchAPIService::class.java)
        fun<T> create(clazz: Class<T>):T{
            //retrofit动态代理，根据传入的接口与接口注解自动生成实现该接口的实例
            return retrofit.create(clazz)
        }
}