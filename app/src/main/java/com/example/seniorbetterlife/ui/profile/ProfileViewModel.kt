package com.example.seniorbetterlife.ui.profile

import android.content.Context
import androidx.lifecycle.*
import com.example.seniorbetterlife.MyApplication
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.utils.Resource
import kotlinx.coroutines.*

class ProfileViewModel: ViewModel() {


    private val firebaseRepository = FirebaseRepository()

    private val _userUpdateStatus = MutableLiveData<Resource<Void>>()
    val userUpdateStatus: LiveData<Resource<Void>> = _userUpdateStatus

    private val _isUserDataAvailable = MutableLiveData<User?>()
    val isUserDataAvailable: LiveData<User?> = _isUserDataAvailable

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

}