package com.spcreations.managetodolist


import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES.R
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toDrawable

class AlarmReceiver : BroadcastReceiver() {

    // val CHANNEL_ID = "TodoListNotificationChannel"

    override fun onReceive(p0: Context?, p1: Intent?) {


        Log.d("TAG", "Alarm fired")
        val taskName = p1!!.getStringExtra("Task_Name").toString()
       val Id = p1.getIntExtra("Id", 0)

        Log.d("TAG", "Id value "+Id)
        val notificationUtils = p0?.let { NotificationUtils(it, taskName, Id) }
        val notification = notificationUtils?.getNotificationBuilder()?.build()
        notificationUtils?.getManager()?.notify(150, notification)


    }
}