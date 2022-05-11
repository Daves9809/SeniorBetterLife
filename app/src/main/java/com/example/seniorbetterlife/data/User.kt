package com.example.seniorbetterlife.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val uid: String? = null,
    val email: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val isSenior: Boolean = true,
    val age: String? = null,
    val sex: String? = null,
    val phoneNumber: String? = null,
    val steps:Int? = null,
    val rating: String? = null)
