package dev.thiagosouto.marvelpoc.data.remote.interceptors

import java.io.IOException

/**
 * Represent internet issues
 */
class InternetConnectionException(e: Exception) : IOException(e)
