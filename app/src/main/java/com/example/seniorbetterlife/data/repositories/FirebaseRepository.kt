package com.example.seniorbetterlife.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.profile.util.Resource
import com.example.seniorbetterlife.profile.util.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

    suspend fun addUserToAuthAndFirestore(user: User, password: String): Resource<AuthResult>{
        return withContext(Dispatchers.IO){
            safeCall {
                //add user to auth
                val registrationResult = auth.createUserWithEmailAndPassword(user.email!!,password).await()
                val uid = auth.currentUser?.uid // pobieranie uid aktualnego uzytkownika
                //add user to firestore
                cloud.collection("users")
                    .document(uid!!)
                    .set(user).await()
                Resource.Success(registrationResult)
            }
        }

    }

    suspend fun loginUser(email: String, password: String): Resource<AuthResult>{
        return withContext(Dispatchers.IO){
            safeCall {
                val loginResult = auth.signInWithEmailAndPassword(email,password).await()
                Resource.Success(loginResult)
            }
        }
    }

    suspend fun updateUser(user: User){
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