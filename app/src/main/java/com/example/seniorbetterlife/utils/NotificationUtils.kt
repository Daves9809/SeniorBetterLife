package com.example.seniorbetterlife.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.seniorbetterlife.MyApplication
import com.example.seniorbetterlife.broadcastReceivers.AlarmReceiver

fun NotificationManagerCompat.createNotification(uniqueID: Int, messageBody: String, applicationContext: Context){
    val notificationIntent = Intent(applicationContext, AlarmReceiver::class.java)
    notificationIntent.putExtra("drugDescription", "Weź lek $messageBody")

    val notificationPendingIntent = PendingIntent.getBroadcast(
        applicationContext, uniqueID, notificationIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}