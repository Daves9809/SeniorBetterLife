package com.example.seniorbetterlife.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.seniorbetterlife.ui.senior.pillReminder.model.Dose
import com.example.seniorbetterlife.ui.senior.pillReminder.model.DoseProperties
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_medicaments")
data class Medicament(
    @PrimaryKey
    @ColumnInfo(name = "medicine")val medicine: String,
    @ColumnInfo(name = "form")val form: Dose,
    @ColumnInfo(name = "frequencyDose")val frequencyDose: Int,
    @ColumnInfo(name = "dailyDose")val dailyDoses: List<DoseProperties>,
    @ColumnInfo(name = "notificationID")val notificationID: Int
) {
    constructor() : this("",Dose.DROPS,-1, emptyList(),-1)
}
