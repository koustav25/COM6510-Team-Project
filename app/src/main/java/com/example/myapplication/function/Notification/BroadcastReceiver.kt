package com.example.myapplication.function.Notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("message") ?: ""
        showNotification(context, message)
    }

    private fun showNotification(context: Context?, message: String) {
        val notificationBuilder = NotificationCompat.Builder(context!!, NotificationScheduler.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("MyApp Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NotificationScheduler.NOTIFICATION_ID, notificationBuilder.build())
    }
}