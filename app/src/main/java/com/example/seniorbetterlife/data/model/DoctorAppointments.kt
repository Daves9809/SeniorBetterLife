package com.example.seniorbetterlife.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_doctorAppointments")
data class DoctorAppointments(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "doctorName")val doctorName : String,
    @ColumnInfo(name = "location")val location: String,
    @ColumnInfo(name = "description")val description: String,
    @ColumnInfo(name = "completed")var completed : Boolean = false
)
