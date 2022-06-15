package com.example.seniorbetterlife.ui.senior.pillReminder.model

import kotlinx.serialization.Serializable

@Serializable
data class DoseProperties(
    val countOfDose: String,
    val hourOfTake: String
) {
    constructor() : this("","")
}

