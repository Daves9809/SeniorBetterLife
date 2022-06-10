package com.example.seniorbetterlife.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.seniorbetterlife.ui.senior.pillReminder.model.Dose
import com.example.seniorbetterlife.ui.senior.pillReminder.model.DoseProperties

@Entity(tableName = "table_medicaments")
data class Medicament(
    @PrimaryKey
    @ColumnInfo(name = "medicine")val medicine: String,
    @ColumnInfo(name = "form")val form: Dose,
    @ColumnInfo(name = "frequencyDose")val frequencyDose: Int,
    @ColumnInfo(name = "dailyDose")val dailyDoses: List<DoseProperties>
)
