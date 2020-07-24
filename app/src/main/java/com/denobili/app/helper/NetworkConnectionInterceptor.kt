package com.denobili.app.helper

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        /*if (!isInternetAvailable())
            throw NoInternetConnection("No Network")*/
        return chain.proceed(chain.request())
    }
    private val applicationContext=context.applicationContext
   /* private fun isInternetAvailable():Boolean{
        val connectivityManager=applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it!=null&& it.isConnected
        }
    }*/
}