package com.example.seniorbetterlife.data.repositories

import android.nfc.Tag
import android.util.Log
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.maps.model.Place
import com.example.seniorbetterlife.maps.model.UserMap
import com.example.seniorbetterlife.util.Resource
import com.example.seniorbetterlife.util.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {

    private val REPO_DEBUG = "REPO_DEBUG"

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    suspend fun getUserData(): User? {
        return withContext(Dispatchers.IO){
                val uid = auth.currentUser?.uid // pobieranie uid aktualnego uzytkownika

                val isDataAvailable = cloud.collection("users") // pobieranie danych z chmury z kolekcji "users"
                    .document(uid!!) // z dokumenty z zaznaczeniem ze uid na ewno nie jest nullem
                    .get().await()
            isDataAvailable.toObject(User::class.java)
        }
    }
    suspend fun getMapLocations(typeOfLocation: String): UserMap?{
        return withContext(Dispatchers.IO){
            val data = cloud.collection("userLocations")
                .document(typeOfLocation)
                .get().await()
            data.toObject(UserMap::class.java)
        }

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

    suspend fun updateUser(user: User): Resource<Void>{
        return withContext(Dispatchers.IO){
            safeCall {
                val uid = auth.currentUser?.uid
                val updatingResult = cloud.collection("users")
                    .document(uid!!)
                    .update(mapOf(
                        "age" to user.age,
                        "name" to user.name,
                        "surname" to user.surname,
                        "phoneNumber" to user.phoneNumber,
                        "sex" to user.sex
                    )).await()
                Resource.Success(updatingResult)
            }
        }

    }

    fun sendUserMap(listOfPoint: List<UserMap>){
        for (userMap in listOfPoint) {
            cloud.collection("userLocations")
                .document(userMap.title)
                .set(userMap)
        }
    }

}