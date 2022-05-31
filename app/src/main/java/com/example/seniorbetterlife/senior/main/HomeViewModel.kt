package com.example.seniorbetterlife.senior.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.senior.helpPart.model.UserTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _userTasks = MutableLiveData<List<UserTask?>>()
    val userTasks: LiveData<List<UserTask?>> = _userTasks

    fun loadUser() {
        viewModelScope.launch(Dispatchers.Main) {
            val user = firebaseRepository.getUserData()
            _user.postValue(user)
        }
    }

    fun getUserTasks(email: String){
        viewModelScope.launch(Dispatchers.Main){
            val isUserTasksRetrieved = firebaseRepository.getUserTasks(email)
            _userTasks.postValue(isUserTasksRetrieved)
        }
    }

    fun setUserTaskVolunteerToNull(userTask: UserTask){
        viewModelScope.launch(Dispatchers.Main) {
            firebaseRepository.updateUserTask(userTask,null)
        }
    }

    fun setUserTaskToCompleted(userTask: UserTask){
        viewModelScope.launch(Dispatchers.Main) {
            firebaseRepository.setUserTaskToCompleted(userTask)
        }
    }
}