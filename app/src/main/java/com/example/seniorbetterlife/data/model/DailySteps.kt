package com.example.seniorbetterlife.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "daily_steps")
data class DailySteps(
    @PrimaryKey
    var day: String,
    var steps: Int
): Parcelable{
    @Ignore
    constructor() : this("",-1)
}
