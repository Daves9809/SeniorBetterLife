package com.example.seniorbetterlife.pedometer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorbetterlife.MyApplication
import com.example.seniorbetterlife.data.model.DailySteps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PedometerViewModel:ViewModel() {

    private val _isDailyStepsRetrieved = MutableLiveData<List<DailySteps>>()
    val isDailyStepsRetrieved: LiveData<List<DailySteps>> = _isDailyStepsRetrieved

    /*fun getDailySteps(){
        viewModelScope.launch(Dispatchers.Main) {
            val listOfDailySteps = dao.getAllAsync()
            _isDailyStepsRetrieved.postValue(listOfDailySteps)
        }
    }*/
}