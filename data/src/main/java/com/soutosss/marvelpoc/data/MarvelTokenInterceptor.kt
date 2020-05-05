package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.ext.toMD5
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class MarvelTokenInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    val generateTimeStamp: () -> Long
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val timestamp = generateTimeStamp()
        val md5Hash = (timestamp.toString() + publicKey + privateKey).toMD5()

        val request = chain.request()
        val url = buildHttpUrl(request, timestamp, md5Hash)
        return chain.proceed(
            request.newBuilder().url(url)
                .build()
        )
    }

    private fun buildHttpUrl(
        request: Request,
        timestamp: Long,
        md5Hash: String
    ) = request.url()
        .newBuilder()
        .addQueryParameter(API_KEY_PARAM, publicKey)
        .addQueryParameter(TIMESTAMP_PARAM, timestamp.toString())
        .addQueryParameter(HASH_PARAM, md5Hash)
        .build()

    companion object {
        private const val API_KEY_PARAM = "apikey"
        private const val HASH_PARAM = "hash"
        private const val TIMESTAMP_PARAM = "ts"
    }
}