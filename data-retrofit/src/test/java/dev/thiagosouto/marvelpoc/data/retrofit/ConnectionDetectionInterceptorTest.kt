package dev.thiagosouto.marvelpoc.data.retrofit

import android.content.Context
import dev.thiagosouto.marvelpoc.data.retrofit.interceptors.ConnectionDetectionInterceptor
import dev.thiagosouto.marvelpoc.data.retrofit.interceptors.InternetConnectionException
import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test

internal class ConnectionDetectionInterceptorTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = mockk(relaxed = true)
    }

    @Test(expected = InternetConnectionException::class)
    fun `intercept should throw InternetConnectionException when there's no internet connection`() {
        val isNetworkNotConnected: (Context) -> Boolean = { true }

        val interceptor =
            ConnectionDetectionInterceptor(
                context,
                isNetworkNotConnected
            )
        interceptor.intercept(mockk())
    }

    @Test
    fun `intercept should execute request when there's internet connection available`() {
        val isNetworkNotConnected: (Context) -> Boolean = { false }
        val interceptor = ConnectionDetectionInterceptor(context, isNetworkNotConnected)

        val request: Request = mockk()
        val response: Response = mockk()
        val chain: Interceptor.Chain = mockk()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        interceptor.intercept(chain)

        every { chain.proceed(request) }
    }
}
