package dev.thiagosouto.marvelpoc.data.remote.interceptors

import java.net.ConnectException
import java.net.UnknownHostException

actual class ExceptionHandler {
    actual suspend inline fun <reified T> handle(func: suspend () -> T): T {
        return try {
            func()
        } catch (e: ConnectException) {
            throw InternetConnectionException(e)
        } catch (e: UnknownHostException) {
            throw InternetConnectionException(e)
        }
    }
}
