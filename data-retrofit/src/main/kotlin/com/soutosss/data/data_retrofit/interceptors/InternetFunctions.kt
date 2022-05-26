package com.soutosss.data.data_retrofit.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

fun isNetworkNotConnected(context: Context): Boolean {
    val connectivityManager: ConnectivityManager? =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isNetworkConnected: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network: Network? = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network) ?: return false

        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    } else {
        val networkInfo =
            connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_WIFI) ?: return false
        networkInfo.isConnected
    }
    return isNetworkConnected.not()
}
