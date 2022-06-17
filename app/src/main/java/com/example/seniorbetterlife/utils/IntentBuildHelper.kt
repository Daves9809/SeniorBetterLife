package com.example.seniorbetterlife.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.seniorbetterlife.broadcastReceivers.AlarmReceiver

class IntentBuildHelper {
    companion object{
        fun createPendingIntent(
            uniqueID: Int,
            messageBody: String,
            applicationContext: Context
        ): PendingIntent {
            val notificationIntent = Intent(applicationContext, AlarmReceiver::class.java)
            notificationIntent.putExtra("drugDescription", "Weź lek $messageBody")
            notificationIntent.putExtra("uniqueID",uniqueID)

            return PendingIntent.getBroadcast(
                applicationContext, uniqueID, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}