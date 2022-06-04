package dev.thiagosouto.webserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

/**
 * Webserver for supporting tests with http calls
 */
class TestWebServer {
    private val server = MockWebServer()
    /**
     * map with Relative path to json path to mock the http responses
     */
    var mapping: Map<String, String> = emptyMap()

    /**
     * Start the webserver
     */
    fun start() {
        server.start()
    }

    /**
     * Stop the webserver
     */
    fun stop() {
        server.close()
    }

    /**
     * initialize the dispatcher for the webserver
     */
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

    /**
     * returns the webserver url
     *
     * @param path is the relative path to merge with the server host
     */
    fun url(path: String = "") = server.url(path).toString()

    private fun String.openFile(): String {
        return TestWebServer::class.java.classLoader!!.getResource(this)!!.readText()
    }

    companion object {
        private const val HTTP_BAD_REQUEST = 400
    }
}
