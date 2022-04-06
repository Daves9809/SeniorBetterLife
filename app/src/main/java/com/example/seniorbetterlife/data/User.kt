package com.example.seniorbetterlife.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val uid: String? = null,
    val email: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val isSenior: Boolean = true,
    val steps:Int? = null)
