package com.example.seniorbetterlife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.senior.helpPart.model.UserTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _usersTasks = MutableLiveData<List<UserTask>>()
    val usersTasks: LiveData<List<UserTask>> = _usersTasks

    private val _usersEmail = MutableLiveData<List<String>>()
    val userEmails: LiveData<List<String>> = _usersEmail


    fun loadUser() {
        viewModelScope.launch(Dispatchers.Main) {
            val user = firebaseRepository.getUserData()
            _user.postValue(user)
        }
    }
    fun getAllUserEmails(){
        viewModelScope.launch(Dispatchers.Main) {
            val listOfEmails = firebaseRepository.getListOfUserEmails()
            _usersEmail.postValue(listOfEmails)
        }
    }

    fun getAllUsersTasks(email: String){
        viewModelScope.launch(Dispatchers.Main) {
                val listOfUserTasks = firebaseRepository.getUserTasks(email)
                _usersTasks.postValue(listOfUserTasks)
        }
    }

    fun updateUserTask(userTask: UserTask, volunteer: User){
        viewModelScope.launch(Dispatchers.Main) {
            firebaseRepository.updateUserTask(userTask, volunteer)
        }
    }
}