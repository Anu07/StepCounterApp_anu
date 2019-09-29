package com.sd.src.stepcounterapp.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast


class SyncService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Device syncing started...", Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Device synced successfully.", Toast.LENGTH_LONG).show()
    }
}