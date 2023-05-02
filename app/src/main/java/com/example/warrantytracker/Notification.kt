package com.example.warrantytracker

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class Notification : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("textExtra").toString()
        val title = intent.getStringExtra("titleExtra").toString()
        val id = intent.getIntExtra("idExtra", 0)
        val notification =
                NotificationCompat.Builder(context, "channel1").setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentText(message).setContentTitle(title).build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(id, notification)
    }
}