package com.example.seniorbetterlife.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
class UserDao {

    @Insert
    fun addUser(user: User) {
    }

    //@Query("SELECT * FROM user_table ORDER BY id ASC")
    //fun readAllData(): LiveData<List<User>> {
    //}
}