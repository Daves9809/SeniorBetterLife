package com.example.seniorbetterlife.data.access

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.seniorbetterlife.data.model.DailySteps
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDataDao {

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
}