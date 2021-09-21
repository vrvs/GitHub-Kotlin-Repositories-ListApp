package com.github.vrvs.githubapp.data.datasource.remote.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.ref.WeakReference

class CacheInterceptor(context: Context) : Interceptor {

    private val context: WeakReference<Context> = WeakReference(context)

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        request = if (hasNetwork(context.get())) {
            request
                .newBuilder()
                .header("Cache-Control", "public, max-age=" + 5)
                .build()
        } else {
            request
                .newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .build()
        }
        return chain.proceed(request)
    }

    private fun hasNetwork(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return if (connectivityManager != null) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                 capabilities != null && (
                     capabilities.hasTransport(
                         NetworkCapabilities.TRANSPORT_CELLULAR
                     ) || capabilities.hasTransport(
                         NetworkCapabilities.TRANSPORT_WIFI
                     ) || capabilities.hasTransport(
                         NetworkCapabilities.TRANSPORT_ETHERNET
                     )
                 )
            } else {
                 val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                 activeNetwork != null && activeNetwork.isConnected
            }
        } else false
    }
}