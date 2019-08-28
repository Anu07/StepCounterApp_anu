package com.sd.src.stepcounterapp

import android.app.Application
import android.content.Context
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import com.fitpolo.support.MokoSupport


class AppApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        instance = this
        MokoSupport.getInstance().init(getApplicationContext());
    }

    fun checkIfHasNetwork(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }

    companion object {

        val TAG = AppApplication::class.java
            .simpleName
        var instance: AppApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun hasNetwork(): Boolean {
            return instance!!.checkIfHasNetwork()
        }
    }
}