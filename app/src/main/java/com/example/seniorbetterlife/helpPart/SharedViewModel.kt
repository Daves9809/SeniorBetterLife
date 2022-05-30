package com.example.seniorbetterlife.helpPart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.helpPart.model.UserTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    private val _isUserDataAvailable = MutableLiveData<User?>()
    val isUserDataAvailable: LiveData<User?> = _isUserDataAvailable

    private val _isUserTasksRetrieved = MutableLiveData<List<UserTask?>>()
    val isUserTaskRetrieved: LiveData<List<UserTask?>> = _isUserTasksRetrieved


    fun getUserData(){
        viewModelScope.launch(Dispatchers.Main) {
            val isDataAvailable = firebaseRepository.getUserData()
            _isUserDataAvailable.postValue(isDataAvailable)
        }
    }

    fun addUserTask(userTask: UserTask, dateWithTime: String) {
        viewModelScope.launch(Dispatchers.Main) {
            firebaseRepository.addUserTask(userTask,dateWithTime)
        }
    }

    fun getUserTasks(email: String){
        viewModelScope.launch(Dispatchers.Main){
            val isUserTasksRetrieved = firebaseRepository.getUserTasks(email)
            _isUserTasksRetrieved.postValue(isUserTasksRetrieved)
        }
    }

    fun deleteUserTask(userTask: UserTask) {
        viewModelScope.launch(Dispatchers.Main) {
            firebaseRepository.deleteUserTask(userTask)
        }
    }

    fun setUserTaskToCompleted(userTask: UserTask){
        viewModelScope.launch(Dispatchers.Main) {
            firebaseRepository.setUserTaskToCompleted(userTask)
        }
    }

}