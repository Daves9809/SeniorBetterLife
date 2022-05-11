package com.example.seniorbetterlife.profile

import androidx.lifecycle.ViewModel
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.data.repositories.FirebaseRepository

class ProfileViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    val user = repository.getUserData()

    fun updateUser(user: User) { repository.updateUser(user) }
}