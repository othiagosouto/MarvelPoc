package dev.thiagosouto.marvelpoc.data.remote.interceptors

expect class ExceptionHandler() {

    suspend inline fun <reified  T> handle(func: suspend () -> T): T
}
