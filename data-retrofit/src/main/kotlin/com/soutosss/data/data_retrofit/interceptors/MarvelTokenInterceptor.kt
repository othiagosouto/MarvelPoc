package com.soutosss.data.data_retrofit.interceptors

import com.soutosss.data.data_retrofit.ext.toMD5
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class MarvelTokenInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    val generateTimeStamp: () -> Long
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val timestamp = generateTimeStamp().toString()
        val md5Hash = (timestamp + privateKey + publicKey).toMD5()

        val request = chain.request()
        val url = buildHttpUrl(request, timestamp, md5Hash)
        return chain.proceed(request.newBuilder().url(url).build())
    }

    private fun buildHttpUrl(request: Request, timestamp: String, md5Hash: String) =
        request.url()
            .newBuilder()
            .addQueryParameter("apikey", publicKey)
            .addQueryParameter("ts", timestamp)
            .addQueryParameter("hash", md5Hash)
            .build()
}