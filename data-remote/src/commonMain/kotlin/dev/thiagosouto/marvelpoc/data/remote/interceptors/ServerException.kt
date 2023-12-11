package dev.thiagosouto.marvelpoc.data.remote.interceptors

class ServerException(code: Int, e: Exception) :
    Exception("HttpException, server response is $code", e)
