package com.example.masterdetaildmt.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.data.datamodels.PokePosition
import com.example.data.service.firestore.FireStore
import com.example.data.service.firestore.IFireStore
import com.example.masterdetaildmt.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask


class LocationBackgroundService : Service() {

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null


    private lateinit var fireStore : IFireStore

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> { start()}
            Actions.STOP.toString() -> { stopSelf()}
            else ->{}
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        createNotificationChannel()
        startTimer{
            getCurrentLocation()
        }
        startForeground(1,getNotification())
    }

    private fun startTimer(action: () -> Unit) {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                action()
            }
        }
        timer!!.schedule(timerTask, 10000 * 6 * 2, 10000)
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        LocationServices.getFusedLocationProviderClient(this).getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token,
            ).addOnSuccessListener { location ->
                location?.let {
                    fireStore = FireStore()
                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val currentDate = sdf.format(Date())
                    fireStore.addNewPokePosition(PokePosition(it.latitude,it.longitude,currentDate),
                        onSuccess = {
                                    Toast.makeText(this,this.getString(R.string.pokelocation_added_success_message),Toast.LENGTH_SHORT).show()
                        },
                        onError = {
                            Toast.makeText(this,this.getString(R.string.pokelocation_added_error_message),Toast.LENGTH_SHORT).show()
                        })

                }
            }.addOnFailureListener { exception ->
            Log.d("EXCEPTION", exception.toString())
        }
    }

    enum class Actions {
        START,
        STOP
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            "POKE_CHANNEL",
            "Location Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(serviceChannel)
    }

    private fun getNotification(): Notification {
        val builder = NotificationCompat.Builder(this, "POKE_CHANNEL")
            .setContentTitle("Pokedex")
            .setContentText("Getting your PokePosition!")
            .setSmallIcon(R.drawable.ic_pokeball)
            .setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            builder.foregroundServiceBehavior = Notification.FOREGROUND_SERVICE_IMMEDIATE
        }
        return builder.build()
    }

    private fun repeatDelayed(delay: Long, action: () -> Unit): Handler {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                action()
                handler.postDelayed(this, delay)
            }
        }, delay)
        return handler
    }
}