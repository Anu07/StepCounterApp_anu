package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.util.Log
import com.sd.src.stepcounterapp.HayaTechApplication.Companion.TAG
import java.text.ParseException


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

    /**
     * returns time from iso timestamp
     */

    fun filterTimefromISOTime(date: String): String {
        //2019-08-20T19:30:00.000Z
        var splitted = ""
        var newDate: Date? = null

        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            try {
                newDate = format.parse(date)
                Log.e("Date", "Parse" + date)
                System.out.println(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            System.out.println(newDate)
            val your_format = SimpleDateFormat("yyyy-MM-dd hh:mm a").format(newDate)
            splitted = your_format.split(" ")[1] + " " + your_format.split(" ")[2]
            println(splitted[1])    //The second part of the splitted string, i.e time


        } catch (e: ParseException) {
            System.out.println(e.toString()) //date format error
        }
        return splitted
    }


    fun getTimefromISOTime(date: String): String {
        //2019-08-20T19:30:00.000Z
        var splitted = ""
        var newDate: Date? = null

        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            try {
                newDate = format.parse(date)
                Log.e("Date", "Parse" + date)
                System.out.println(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            System.out.println(newDate)
            val your_format = SimpleDateFormat("yyyy-MM-dd HH:mm").format(newDate)
            splitted = your_format.split(" ")[1]
            println(splitted[1])    //The second part of the splitted string, i.e time


        } catch (e: ParseException) {
            System.out.println(e.toString()) //date format error
        }
        return splitted
    }

    fun formateDateFromstring(
        inputFormat: String,
        outputFormat: String,
        inputDate: String
    ): String {

        var parsed: Date? = null
        var outputDate = ""

        val df_input = SimpleDateFormat(inputFormat, java.util.Locale.getDefault())
        val df_output = SimpleDateFormat(outputFormat, java.util.Locale.getDefault())

        try {
            parsed = df_input.parse(inputDate)
            outputDate = df_output.format(parsed)

        } catch (e: ParseException) {
            Log.e(TAG, "ParseException - dateFormat")
        }

        return outputDate

    }


    fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(context)
        return packageNames.contains(context.packageName)
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
        return gps && network
    }

    fun retryInternet(mContext: Context): Boolean {
        var connectivity = mContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        )
        if (connectivity != null) {
            val connMgr = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        return false
    }


}
