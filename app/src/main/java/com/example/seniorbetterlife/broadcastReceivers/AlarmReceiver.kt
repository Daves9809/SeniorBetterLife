package com.example.seniorbetterlife.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.seniorbetterlife.MyApplication
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.utils.Constants
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val description = intent?.getStringExtra("drugDescription") ?: "description is null"
        val uniqueID = intent?.getIntExtra("uniqueID",0) ?: 0

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            MyApplication.getContext(), Constants.CHANNEL_PILL_REMIND_ID
        )
            .setSmallIcon(R.drawable.medicine)
            .setContentTitle("Przypomnienie")
            .setContentText(description)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManagerCompat = NotificationManagerCompat.from(
            MyApplication.getContext()
        )
        notificationManagerCompat.notify(uniqueID, builder.build())
        Log.d("UNIQUEID","$uniqueID")
    }
}