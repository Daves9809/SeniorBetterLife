package com.example.seniorbetterlife

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        lateinit var instance: MyApplication
        lateinit var dataStore: DataStore<Preferences>
        fun getContext(): Context{
            return instance.applicationContext
        }
    }
}