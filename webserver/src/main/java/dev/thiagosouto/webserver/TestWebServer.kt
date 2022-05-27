package dev.thiagosouto.webserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

class TestWebServer {
    private val server = MockWebServer()
    var mapping: Map<String, String> = emptyMap()

    fun start() {
        server.start()
    }

    fun stop() {
        server.close()
    }

    fun initDispatcher() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = mapping[request.path]?.openFile()

                return body?.let { MockResponse().setBody(body) } ?: MockResponse().setResponseCode(
                    HTTP_BAD_REQUEST
                )
            }
        }
    }

    fun url(path: String = "") = server.url(path).toString()

    private fun String.openFile(): String {
        return TestWebServer::class.java.classLoader!!.getResource(this)!!.readText()
    }

    companion object {
        private const val HTTP_BAD_REQUEST = 400
    }
}
