package com.example.seniorbetterlife.data.access

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.util.Constants

@Database(entities = [DailySteps::class], version = 3)
abstract class MyDatabase: RoomDatabase() {

    abstract fun myDataDao(): MyDataDao

    companion object{
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    Constants.RUNNING_DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return  instance
            }
        }
    }

}