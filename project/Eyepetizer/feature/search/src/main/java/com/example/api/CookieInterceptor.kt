package com.example.api

import okhttp3.Interceptor
import okhttp3.Response
import kotlin.text.get

class CookieInterceptor(private val udidStore : KyUdidStore): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Cookie","ky_udid=${udidStore.get()}").build()
        return chain.proceed(request)
    }

}