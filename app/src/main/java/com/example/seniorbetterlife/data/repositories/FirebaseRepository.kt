package com.example.seniorbetterlife.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seniorbetterlife.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository {

    private val REPO_DEBUG = "REPO_DEBUG"

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    fun getUserData(): LiveData<User>{
        val cloudResult = MutableLiveData<User>()
        val uid = auth.currentUser?.uid // pobieranie uid aktualnego uzytkownika

        cloud.collection("users") // pobieranie danych z chmury z kolekcji "users"
            .document(uid!!) // z dokumenty z zaznaczeniem ze uid na ewno nie jest nullem
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java) // stworzenie z otrzymanych danych obiektu User
                cloudResult.postValue(user!!)
            }.addOnFailureListener {
                Log.d(REPO_DEBUG,it.message.toString())
            }
        return cloudResult
    }

    fun addUser(user: User){
        val uid = auth.currentUser?.uid // pobieranie uid aktualnego uzytkownika
        cloud.collection("users")
            .document(uid!!)
            .set(user)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG,"User added succesfully")
            }.addOnFailureListener {
                Log.d(REPO_DEBUG, "Error: " + it.message.toString())
            }
    }
    fun updateUser(user: User){
        val uid = auth.currentUser?.uid
        cloud.collection("users")
            .document(uid!!)
            .update(mapOf(
                "age" to user.age,
                "name" to user.name,
                "surname" to user.surname,
                "phoneNumber" to user.phoneNumber,
                "sex" to user.sex
            )).addOnSuccessListener {
                Log.d(REPO_DEBUG,"User added succesfully")
            }.addOnFailureListener {
                Log.d(REPO_DEBUG, "Error: " + it.message.toString())
            }
    }
}