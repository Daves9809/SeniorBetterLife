package com.example.seniorbetterlife.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.services.PedometerService
import com.example.seniorbetterlife.util.Constants
import com.example.seniorbetterlife.util.DateFormatter
import com.example.seniorbetterlife.util.SettingPrefs

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {

                goAsync(){
                    val user: User? = FirebaseRepository().getUserData()
                    if (user != null) {
                        if(user.senior){
                            val serviceIntent = Intent(context, PedometerService::class.java)
                            Log.d("MyBroadcastReceiver", "SERVICE RESTARTED = true")
                            context?.startForegroundService(serviceIntent)
                        }
                    }else
                        Log.d("MyBroadcastReceiver", "CurrentUser = null or senior = false")

                    SettingPrefs(context!!).setIsReboot(true)

                }
            }
        }
    }



}