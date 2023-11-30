package dev.thiagosouto.marvelpoc.data.retrofit.interceptors

class HttpException(code: Int): Exception("HttpException, server response is $code")