package com.example.seniorbetterlife.data.repositories

import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.data.UserDao

class UserRepository(private val userDAO: UserDao) {

    //val readAllData: LiveData<List<User>> = userDAO.readAllData()

    fun addUser(user: User){
        userDAO.addUser(user)
    }
}