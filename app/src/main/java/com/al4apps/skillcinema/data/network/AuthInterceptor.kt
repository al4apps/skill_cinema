package com.al4apps.skillcinema.data.network

import com.al4apps.skillcinema.data.settings.Settings
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedHeader = originalRequest.headers.newBuilder()
            .add(name = "X-API-KEY", value = Settings.API_KEY_2)
            .build()
        val modifiedRequest = originalRequest.newBuilder()
            .headers(modifiedHeader)
            .build()

        return chain.proceed(modifiedRequest)
    }
}