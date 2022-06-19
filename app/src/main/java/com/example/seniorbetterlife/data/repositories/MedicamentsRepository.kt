package com.example.seniorbetterlife.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.seniorbetterlife.data.access.MyDataDao
import com.example.seniorbetterlife.data.model.Medicament
import kotlinx.coroutines.flow.Flow

class MedicamentsRepository(private val myDataDao: MyDataDao) {

    val medicaments: Flow<List<Medicament>> = myDataDao.getAllMedicamentsAsync()

    suspend fun insertMedicament(medicament: Medicament) {
        Log.d("MedicamentsRepository", "insertMedicament = $medicament")
        myDataDao.insertMedicament(medicament)
    }

    suspend fun deleteMedicament(medicament: Medicament){
        Log.d("MedicamentsRepository", "Medicament = $medicament deleted")
        myDataDao.deleteMedicament(medicament)
    }

}