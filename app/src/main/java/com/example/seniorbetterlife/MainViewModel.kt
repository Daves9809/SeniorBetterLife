package com.example.seniorbetterlife

import android.app.Application
import androidx.lifecycle.*
import com.example.seniorbetterlife.data.access.MyDatabase
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.data.repositories.RoomRepository
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()
    private val roomRepository: RoomRepository

    init {
        val myDataDao = MyDatabase.getDatabase(application).myDataDao()
        roomRepository = RoomRepository(myDataDao)
    }

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _usersTasks = MutableLiveData<List<UserTask>>()
    val usersTasks: LiveData<List<UserTask>> = _usersTasks

    private val _usersEmail = MutableLiveData<List<String>>()
    val userEmails: LiveData<List<String>> = _usersEmail

    private val _dailySteps = MutableLiveData<List<DailySteps?>>()
    val dailySteps: LiveData<List<DailySteps?>> = _dailySteps


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
    fun saveDailySteps(user: User, dailySteps: List<DailySteps?>){
        viewModelScope.launch(Dispatchers.Main) {
            firebaseRepository.updateUserDailySteps(user, dailySteps)
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
    fun getDailySteps() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfDailySteps = roomRepository.getDailySteps()
            _dailySteps.postValue(listOfDailySteps)
        }
    }

    fun clearLocalDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.clearDatabase()
        }
    }
}