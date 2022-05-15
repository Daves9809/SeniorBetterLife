package com.example.seniorbetterlife.profile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.seniorbetterlife.activities.MainActivity
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import kotlinx.coroutines.*

class ProfileViewModel: ViewModel() {


    private val repository = FirebaseRepository()

    val user = repository.getUserData()
    val userr = MutableLiveData<User>()

    /*var job: Job? = null

    fun getUserData() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getUserData()
            withContext(Dispatchers.Main) {
                userr.postValue(response.value)
                Log.i("ProfileViewModel", userr.value.toString())
            }
        }
    }
     */

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }

    private var stepsCount = 0

    private val _stepsCount = MutableLiveData<String>("Liczba krokow: $stepsCount")

    val steps: LiveData<String>
        get() = _stepsCount


    suspend fun updateSteps() {
            _stepsCount.value = MainActivity.applicationContext()
                .getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getInt("key1",0).toString()
    }
}