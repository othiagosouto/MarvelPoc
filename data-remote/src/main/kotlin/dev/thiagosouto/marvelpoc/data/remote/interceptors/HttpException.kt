package dev.thiagosouto.marvelpoc.data.remote.interceptors

class HttpException(code: Int): Exception("HttpException, server response is $code")
