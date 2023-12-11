package dev.thiagosouto.marvelpoc.data.remote.interceptors

actual class ExceptionHandler {
    actual suspend inline fun <reified T> handle(func: suspend () -> T): T {
        return func()
    }
}
