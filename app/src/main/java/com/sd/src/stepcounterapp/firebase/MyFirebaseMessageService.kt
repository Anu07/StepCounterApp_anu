package com.sd.src.stepcounterapp.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sd.src.stepcounterapp.AppApplication.Companion.TAG
import com.sd.src.stepcounterapp.R
import org.json.JSONException
import org.json.JSONObject



class MyFirebaseMessageService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }
        var data = remoteMessage.data
        val jsonObject = JSONObject(data)
        var body = ""
        var title = ""

        try {
            Log.e("data", jsonObject.toString())
            body = jsonObject.getString("message")
            title = jsonObject.getString("Title")
            Log.e("body", body)
            Log.e("title", title)
        } catch (e: JSONException) {
            Log.e("exception>>>", e.toString() + "")
            e.printStackTrace()
            var body = remoteMessage.notification!!.body
            Log.e("body>>>", body + "")
        }


        showNotification(title,body)
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "hayatech_channel_id"
        val channelName = "Hayatech Channel"
        val importance = NotificationManager.IMPORTANCE_LOW
        val notifyId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = Notification.Builder(this)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.app_icon_new)
            .build()
        notificationManager.notify(notifyId, notification);
    }

}