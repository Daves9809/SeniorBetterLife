package com.example.seniorbetterlife.util

import android.app.Activity
import android.content.Context

object Constants {

    fun getSharePref(context: Context) =
    context.getSharedPreferences("SharedPreferences", Activity.MODE_PRIVATE)

    fun editor(context: Context) = getSharePref(context).edit()

    const val RUNNING_DATABASE_NAME = "running_db"

    const val NOTIFICATION_CHANNEL_ID = "pedometer_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Pedometer"
    const val NOTIFICATION_ID = 1

    const val ACTION_SHOW_PEDOMETER_FRAGMENT = "ACTION_SHOW_PEDOMETER_FRAGMENT"

    const val ACTION_START_FOREGROUND_ACTION = "ACTION_START_FOREGROUND_ACTION"
    const val ACTION_STOP_FOREGROUND_ACTION = "ACTION_STOP_FOREGROUND_ACTION"

}