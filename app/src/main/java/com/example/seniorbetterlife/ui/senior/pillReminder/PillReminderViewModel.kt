package com.example.seniorbetterlife.ui.senior.pillReminder

import android.app.Application
import androidx.lifecycle.*
import com.example.seniorbetterlife.data.access.MyDatabase
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.data.repositories.MedicamentsRepository
import com.example.seniorbetterlife.data.repositories.RoomRepository
import com.example.seniorbetterlife.ui.senior.pillReminder.model.Dose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PillReminderViewModel(application: Application): AndroidViewModel(application) {

    private val medicamentsRepository: MedicamentsRepository
    private val firebaseRepository = FirebaseRepository()

    init {
        val myDataDao = MyDatabase.getDatabase(application).myDataDao()
        medicamentsRepository = MedicamentsRepository(myDataDao)
    }

    private val _drugName = MutableLiveData<String>()
    val drugName: LiveData<String> = _drugName

    private val _dose = MutableLiveData<Dose>()
    val dose: LiveData<Dose> = _dose

    private val _frequency = MutableLiveData<Int>()
    val frequency: LiveData<Int> = _frequency

    val medicaments:LiveData<List<Medicament>> = medicamentsRepository.medicaments.asLiveData()

    fun chooseDose(doseDescription: String) {
         val dose = when(doseDescription){
            Dose.DROPS.description -> Dose.DROPS
            Dose.MILIGRAMS.description -> Dose.MILIGRAMS
            Dose.TABLETS.description -> Dose.TABLETS
            else -> Dose.TABLETS
        }
        _dose.postValue(dose)
    }

    fun setFrequency(freq: Int) = _frequency.postValue(freq)

    fun setName(drugName: String) = _drugName.postValue(drugName)

    fun addMedicament(medicament: Medicament) {
        viewModelScope.launch(Dispatchers.Main) {
            medicamentsRepository.insertMedicament(medicament)
        }
    }

    fun deleteMedicament(medicament: Medicament){
        viewModelScope.launch {
            medicamentsRepository.deleteMedicament(medicament)
        }
    }

    fun saveMedicamentsToFirebase(medicaments: List<Medicament>){
        viewModelScope.launch {
            val user = firebaseRepository.getUserData()
            firebaseRepository.addMedicamentsToFirebase(user!!,medicaments)
        }
    }
}