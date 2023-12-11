package dev.thiagosouto.marvelpoc.data.remote.interceptors

class ClientException(code: Int, e: Exception) :
    Exception("HttpException, server response is $code", e)
