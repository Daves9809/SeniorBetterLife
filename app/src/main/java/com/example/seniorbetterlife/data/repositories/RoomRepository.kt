package com.example.seniorbetterlife.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.seniorbetterlife.data.access.MyDataDao
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.User
import kotlinx.coroutines.flow.Flow


class RoomRepository(private val myDataDao: MyDataDao) {

    val listOfDailySteps: Flow<List<DailySteps>> = myDataDao.getAllDailySteps()

    suspend fun getDailySteps(): List<DailySteps?>{
        Log.d("RoomRepository","GetDailySteps")
        return myDataDao.getAllAsync()
    }

    suspend fun addDailySteps(dailySteps: DailySteps){
        Log.d("RoomRepository","AddDailySteps = $dailySteps")
        myDataDao.insert(dailySteps)
    }

    suspend fun updateSteps(dailySteps: DailySteps){
        Log.d("RoomRepository","UpdateSteps = $dailySteps")
        myDataDao.updateSteps(dailySteps)
    }

    suspend fun clearDatabase(){
        Log.d("RoomRepository","Clear Local Room Database")
        myDataDao.clear()
    }
}