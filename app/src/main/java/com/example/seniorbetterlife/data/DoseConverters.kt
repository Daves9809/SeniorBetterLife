package com.example.seniorbetterlife.data

import androidx.room.TypeConverter
import com.example.seniorbetterlife.ui.senior.pillReminder.model.DoseProperties
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DoseConverters {
    @TypeConverter
    fun fromList(value : List<DoseProperties>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<DoseProperties>>(value)
}