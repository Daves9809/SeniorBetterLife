package com.example.seniorbetterlife.profile

import android.content.Context
import androidx.lifecycle.*
import com.example.seniorbetterlife.activities.MainActivity
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.profile.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.*

class ProfileViewModel: ViewModel() {


    private val firebaseRepository = FirebaseRepository()

    //in activities we're going to observe status
    private val _userUpdateStatus = MutableLiveData<Resource<Void>>()
    val userUpdateStatus: LiveData<Resource<Void>> = _userUpdateStatus

    private val _isUserDataAvailable = MutableLiveData<User?>()
    val isUserDataAvailable: LiveData<User?> = _isUserDataAvailable

    //val user = firebaseRepository.getUserData()

    fun getUserData(){
        viewModelScope.launch(Dispatchers.Main) {
            val isDataAvailable = firebaseRepository.getUserData()
            _isUserDataAvailable.postValue(isDataAvailable)
        }
    }

    fun updateUser(user: User){
        _userUpdateStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val updateUser = firebaseRepository.updateUser(user)
            _userUpdateStatus.postValue(updateUser)
        }
    }




    private var stepsCount = 0
    private val _stepsCount = MutableLiveData<String>("Liczba krokow: $stepsCount")
    val steps: LiveData<String>
        get() = _stepsCount

    fun updateSteps() {
            _stepsCount.value = MainActivity.applicationContext()
                .getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getInt("key1",0).toString()
    }
}