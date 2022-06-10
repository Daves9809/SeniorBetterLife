package com.example.seniorbetterlife.data.access

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.seniorbetterlife.data.DoseConverters
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.DoctorAppointments
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.util.Constants

@Database(entities = [DailySteps::class, Medicament::class, DoctorAppointments::class], version = 6)
@TypeConverters(DoseConverters::class)
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