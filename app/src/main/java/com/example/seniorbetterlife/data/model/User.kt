package com.example.seniorbetterlife.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val uid: String? = null,
    val email: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val senior: Boolean = true,
    val age: String? = null,
    val sex: String? = null,
    val phoneNumber: String? = null,
    val dailySteps: List<DailySteps?> = emptyList())
