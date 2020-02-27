package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

import java.text.SimpleDateFormat
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.fitpolo.support.MokoSupport
import com.sd.src.stepcounterapp.HayaTechApplication.Companion.TAG
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.service.MokoService
import java.text.ParseException
import java.util.*
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import com.sd.src.stepcounterapp.HayaTechApplication


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
                Log.e("Date", "Parse$date")
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


    /**
     * Whether the location service is enabled on the phone. If it is not enabled, then all apps will not be able to use the location function.
     */
    fun isLocServiceEnable(context: Context): Boolean {
        return checkIfGPSonOrNot(context)
        /* val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
         val gps = Settings.Secure.getString(
             context.contentResolver,
             Settings.Secure.LOCATION_PROVIDERS_ALLOWED
         );
         val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
         return (gps.contains("gps")) && network*/
    }

    fun checkIfGPSonOrNot(context: Context): Boolean {
        val manager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun retryInternet(mContext: Context): Boolean {
        var connectivity = mContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        )
        if (connectivity != null) {
            val connMgr =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        return false
    }


    /**
     * get Current date
     */

    fun getCurrentDate(): String? {
        var formatter = SimpleDateFormat("dd/MM/yyyy")
        var date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }


    /**
     * get Current date
     */

    fun getWearCurrentDate(): String? {
        var formatter = SimpleDateFormat("yyyy-MM-dd")
        var date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }


    /**
     * fetch offset
     */

    fun parseTimeOffset(): Int {

        val tz = TimeZone.getDefault()
        val cal = Calendar.getInstance(tz)
        val offsetInMillis = tz.getOffset(cal.timeInMillis)

        var offset =
            String.format(
                "%02d:%02d",
                Math.abs(offsetInMillis / 3600000),
                Math.abs(offsetInMillis / 60000 % 60)
            )
        var minutesOffset = ((offset.split(":")[0].toInt() * 60) + offset.split(":")[1].toInt())
        offset = (if (offsetInMillis >= 0) "+" else "-") + minutesOffset
        Log.i("minutes", "offset " + offset.toInt())
        return offset.toInt()
    }

    /**
     * logout
     */

    fun purchaseNowdialog(
        context: Context
    ): AlertDialog.Builder {
        // Clearing all data from Shared Preferences
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to purchase this product?")
            .setCancelable(false)
            .setTitle("Product Purchase")
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        return builder
    }


}
