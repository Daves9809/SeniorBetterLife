package com.example.seniorbetterlife.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.maps.model.UserMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    private val _isUserMapDataAvailable = MutableLiveData<UserMap?>()
    val isUserDataAvailable: LiveData<UserMap?> = _isUserMapDataAvailable

    fun getUserMaps(typeOfLocation: String){
        viewModelScope.launch(Dispatchers.Main){
            val isDataAvailable = firebaseRepository.getMapLocations(typeOfLocation)
            _isUserMapDataAvailable.postValue(isDataAvailable)
        }
    }
}