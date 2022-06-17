package com.example.seniorbetterlife.ui.loginRegister

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import com.example.seniorbetterlife.data.access.MyDatabase
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.data.repositories.MedicamentsRepository
import com.example.seniorbetterlife.data.repositories.RoomRepository
import com.example.seniorbetterlife.utils.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegLogViewModel(application: Application): AndroidViewModel(application) {
    private val firebaseRepository = FirebaseRepository()
    private val roomRepository: RoomRepository
    private val medicamentsRepository: MedicamentsRepository

    init {
        val myDataDao = MyDatabase.getDatabase(application).myDataDao()
        roomRepository = RoomRepository(myDataDao)
        medicamentsRepository = MedicamentsRepository(myDataDao)
    }

    private val _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>()
    val userRegistrationStatus: LiveData<Resource<AuthResult>> = _userRegistrationStatus

    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    val medicaments:LiveData<List<Medicament>> = medicamentsRepository.medicaments.asLiveData()

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

    fun loadUser() {
        viewModelScope.launch(Dispatchers.Main) {
            val user = firebaseRepository.getUserData()
            _user.postValue(user)
        }
    }
    fun setRoomDailySteps(listOfDailySteps: List<DailySteps?>) {
        viewModelScope.launch(Dispatchers.IO) {
            if(listOfDailySteps.isNotEmpty()){
                for(dailySteps in listOfDailySteps){
                    roomRepository.addDailySteps(dailySteps!!)
                }
            }
        }
    }

    fun setRoomMedicaments(medicaments: List<Medicament>?) {
        viewModelScope.launch {
            if (medicaments != null) {
                roomRepository.addMedicaments(medicaments)
            }
        }
    }
}