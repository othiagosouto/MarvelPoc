package dev.thiagosouto.marvelpoc.data.remote.interceptors

import io.ktor.utils.io.errors.IOException

/**
 * Represent internet issues
 */
open class InternetConnectionException(e: Exception) :
    IOException("InternetConnectionException ", e)
