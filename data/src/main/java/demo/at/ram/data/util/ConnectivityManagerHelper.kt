package demo.at.ram.data.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object ConnectivityManagerHelper {
    fun isCurrentlyConnected(connectivityManager: ConnectivityManager): Boolean {
        return connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(
                it
            )?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
            ?: run { false }
    }

}