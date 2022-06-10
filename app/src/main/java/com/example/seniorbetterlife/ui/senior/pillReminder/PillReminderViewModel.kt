package com.example.seniorbetterlife.ui.senior.pillReminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.ui.senior.pillReminder.model.Dose

class PillReminderViewModel: ViewModel() {

    private val _drugName = MutableLiveData<String>()
    val drugName: LiveData<String> = _drugName

    private val _dose = MutableLiveData<Dose>()
    val dose: LiveData<Dose> = _dose

    private val _frequency = MutableLiveData<Int>()
    val frequency: LiveData<Int> = _frequency

    private val _medicament = MutableLiveData<Medicament>()
    val medicament: LiveData<Medicament> = _medicament

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

    fun addMedicament(medicament: Medicament) = _medicament.postValue(medicament)
}