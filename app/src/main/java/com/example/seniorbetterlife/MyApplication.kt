package com.example.seniorbetterlife

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.seniorbetterlife.data.access.MyDatabase
import com.example.seniorbetterlife.util.Constants

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    companion object{
        lateinit var instance: MyApplication
        fun getContext(): Context{
            return instance.applicationContext
        }
    }
}