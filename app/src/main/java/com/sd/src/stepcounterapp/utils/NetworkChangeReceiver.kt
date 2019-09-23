package com.sd.src.stepcounterapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log


class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connMgr = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val wifi = connMgr
            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        val mobile = connMgr
            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        if (wifi.isAvailable || mobile.isAvailable) {
            // Do something

            Log.d("Network Available ", "Flag No 1")
        }
    }
}