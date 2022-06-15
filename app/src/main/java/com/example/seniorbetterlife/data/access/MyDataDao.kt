package com.example.seniorbetterlife.data.access

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.Medicament
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDataDao {

    //DailySteps
    @Query("SELECT * FROM daily_steps")
    suspend fun getAllAsync(): List<DailySteps?>

    @Query("SELECT * FROM daily_steps")
    fun getAllDailySteps(): Flow<List<DailySteps>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailySteps: DailySteps)

    @Update
    suspend fun updateSteps(dailySteps: DailySteps)

    @Query("DELETE FROM daily_steps")
    suspend fun clear()

    //Medicament
    @Query("SELECT * FROM table_medicaments")
    fun getAllMedicamentsAsync(): Flow<List<Medicament>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicament(medicament: Medicament)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMedicaments(medicaments: List<Medicament>)

    @Delete
    suspend fun deleteMedicament(medicament: Medicament)

    @Query("DELETE FROM table_medicaments")
    suspend fun clearMedicaments()
}