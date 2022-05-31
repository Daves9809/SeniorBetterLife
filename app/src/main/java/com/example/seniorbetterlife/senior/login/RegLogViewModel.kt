package com.example.seniorbetterlife.senior.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegLogViewModel: ViewModel() {

    //in activities we're going to observe status
    private val _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>()
    val userRegistrationStatus: LiveData<Resource<AuthResult>> = _userRegistrationStatus

    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    private val firebaseRepository = FirebaseRepository()

    fun createUser(email: String, password: String, repeatedPassword: String,isSenior: Boolean) {
        var error =
            if (email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty() ) {
                "Empty Strings"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "Not a valid Email"
            } else null
        error?.let {
            _userRegistrationStatus.postValue(Resource.Error(it))
            return
        }
        _userRegistrationStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val registerResult = firebaseRepository.addUserToAuthAndFirestore(User(email = email,senior = isSenior),password)
            _userRegistrationStatus.postValue(registerResult)
        }
    }

    fun loginUser(email:String, password: String){
        var error =
            if(email.isEmpty() || password.isEmpty()){
                "Empty Strings"
            } else null
        error?.let {
            _userSignUpStatus.postValue(Resource.Error(it))
            return
        }
        _userSignUpStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val loginResult = firebaseRepository.loginUser(email,password)
            _userSignUpStatus.postValue(loginResult)
        }
    }
}