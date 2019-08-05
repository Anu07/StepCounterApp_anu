package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.core.content.ContextCompat.startActivity
import android.text.method.TextKeyListener.clear



object Utils {


    /**
     * 字符串时间转换成calendar
     *
     * @param strDate
     * @param pattern
     * @return
     */

    fun strDate2Calendar(strDate: String, pattern: String): Calendar? {
        val sdf = SimpleDateFormat(pattern, Locale.US)
        try {
            val date = sdf.parse(strDate)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


    fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(context)
        return if (packageNames.contains(context.packageName)) {
            true
        } else false
    }

    fun openNotificationListenSettings(context: Context) {
        try {
            val intent: Intent
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            } else {
                intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    fun isLocServiceEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return if (gps || network) {
            true
        } else false
    }


}
