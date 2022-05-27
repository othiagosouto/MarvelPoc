package com.soutosss.data.retrofit.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

internal class ConnectionDetectionInterceptor(
    private val context: Context,
    private val isNetworkNotConnected: (Context) -> Boolean
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isNetworkNotConnected(context)) {
            throw InternetConnectionException()
        }
        return chain.proceed(chain.request())
    }
}
