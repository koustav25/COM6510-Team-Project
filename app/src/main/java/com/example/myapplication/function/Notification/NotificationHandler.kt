package com.example.myapplication.function.Notification

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Locale

object Notification {
        @SuppressLint("SuspiciousIndentation")
        @RequiresApi(Build.VERSION_CODES.O)
        fun SetNotification(time: String, context: Context, message:String) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = dateFormat.parse(time) ?: return

            val handler = Handler(Looper.getMainLooper())
            //val delay = date.time - System.currentTimeMillis() - 86400000 // Calculate delay till scheduled time
            val delay = date.time - System.currentTimeMillis()
            Log.d("time","$delay")
            //val delay = 300000L
                handler.postDelayed({
                    NotificationScheduler.createNotificationChannel(context)
                    NotificationScheduler.scheduleNotification(
                        context,
                        date,
                        message
                    )
                }, delay)
        }
}