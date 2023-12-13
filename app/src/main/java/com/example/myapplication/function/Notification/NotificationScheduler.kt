package com.example.myapplication.function.Notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import java.util.Date

class NotificationScheduler {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "MyAppChannel"
        const val NOTIFICATION_ID = 1

        fun scheduleNotification(context: Context, date: Date, message: String) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.putExtra("message", message)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            // Set the alarm to trigger at the specified date
            alarmManager.set(AlarmManager.RTC, date.time, pendingIntent)

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "MyApp Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}