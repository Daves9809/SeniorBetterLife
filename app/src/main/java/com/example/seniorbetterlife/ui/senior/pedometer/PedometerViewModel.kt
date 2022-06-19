package com.example.seniorbetterlife.ui.senior.pedometer

import android.app.Application
import androidx.lifecycle.*
import com.example.seniorbetterlife.data.access.MyDatabase
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.repositories.RoomRepository

class PedometerViewModel(application: Application):AndroidViewModel(application) {

    private val roomRepository: RoomRepository

    init {
        val myDataDao = MyDatabase.getDatabase(application).myDataDao()
        roomRepository = RoomRepository(myDataDao)
    }

    val listOfDailySteps: LiveData<List<DailySteps>> = roomRepository.listOfDailySteps.asLiveData()
}