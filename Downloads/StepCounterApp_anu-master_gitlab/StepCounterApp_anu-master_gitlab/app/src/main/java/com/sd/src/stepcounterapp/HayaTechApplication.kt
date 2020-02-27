package com.sd.src.stepcounterapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.fitpolo.support.MokoSupport
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.FIREBASETOKEN


class HayaTechApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        instance = this
        MokoSupport.getInstance().init(applicationContext)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                Log.e("Token",""+token)
                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, token)
                if (token != null) {
                        SharedPreferencesManager.setString(applicationContext(),token, FIREBASETOKEN)
                }
//                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
    }

    fun checkIfHasNetwork(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }

    /**
     * logout
     */

    fun logoutUser() {
        // Clearing all data from Shared Preferences
//                MokoSupport.getInstance().disConnectBle()
                SharedPreferencesManager.logout(applicationContext)
    }


    companion object {

        val TAG = HayaTechApplication::class.java
            .simpleName
        var instance: HayaTechApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun hasNetwork(): Boolean {
            return instance!!.checkIfHasNetwork()
        }


    }
}