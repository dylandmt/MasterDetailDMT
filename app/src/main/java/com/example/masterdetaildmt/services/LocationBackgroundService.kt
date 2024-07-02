package com.example.masterdetaildmt.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

abstract class LocationBackgroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        performLongTask()
        return START_STICKY // If the service is killed, it will be automatically restarted
    }

    private fun performLongTask() {
        // Imagine doing something that takes a long time here
        Thread.sleep(5000)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun log(str:String){
        Log.d("TAG", "log: $str")
    }


}

