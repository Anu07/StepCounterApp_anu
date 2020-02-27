package com.sd.src.stepcounterapp.activities

import androidx.core.content.ContextCompat


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.SystemClock
import com.fitpolo.support.log.LogModule

open class Basefit : Activity() {

    // 记录上次页面控件点击时间,屏蔽无效点击事件
    protected var mLastOnClickTime: Long = 0

    val isWindowLocked: Boolean
        get() {
            val current = SystemClock.elapsedRealtime()
            if (current - mLastOnClickTime > 500) {
                mLastOnClickTime = current
                return false
            } else {
                return true
            }
        }

    val isWriteStoragePermissionOpen: Boolean
        get() =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) === PackageManager.PERMISSION_GRANTED

    val isLocationPermissionOpen: Boolean
        get() =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val intent = Intent(this, GuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            return
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogModule.i("onConfigurationChanged...")
        finish()
    }
}
