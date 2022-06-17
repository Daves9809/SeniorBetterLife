package com.example.seniorbetterlife.data.repositories

import android.util.Log
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserTask
import com.example.seniorbetterlife.ui.senior.maps.model.UserMap
import com.example.seniorbetterlife.utils.Resource
import com.example.seniorbetterlife.utils.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {

    private val TAG = "REPO_DEBUG"

    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()


    suspend fun getUserData(): User? {
        return withContext(Dispatchers.IO) {
            val uid = auth.currentUser?.uid // pobieranie uid aktualnego uzytkownika

            val isDataAvailable =
                cloud.collection("users") // pobieranie danych z chmury z kolekcji "users"
                    .document(uid!!) // z dokumenty z zaznaczeniem ze uid na ewno nie jest nullem
                    .get().await()
            isDataAvailable.toObject(User::class.java)
        }
    }

    suspend fun getMapLocations(typeOfLocation: String): UserMap? {
        return withContext(Dispatchers.IO) {
            val data = cloud.collection("userLocations")
                .document(typeOfLocation)
                .get().await()
            data.toObject(UserMap::class.java)
        }

    }

    suspend fun addUserToAuthAndFirestore(user: User, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                //add user to auth
                val registrationResult =
                    auth.createUserWithEmailAndPassword(user.email!!, password).await()
                val uid = auth.currentUser?.uid // pobieranie uid aktualnego uzytkownika
                //add user to firestore
                cloud.collection("users")
                    .document(uid!!)
                    .set(User(uid = uid, email = user.email, senior = user.senior)).await()
                Resource.Success(registrationResult)
            }
        }

    }

    suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val loginResult = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(loginResult)
            }
        }
    }

    suspend fun updateUser(user: User): Resource<Void> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val uid = auth.currentUser?.uid
                val updatingResult = cloud.collection("users")
                    .document(uid!!)
                    .update(
                        mapOf(
                            "age" to user.age,
                            "name" to user.name,
                            "surname" to user.surname,
                            "phoneNumber" to user.phoneNumber,
                            "sex" to user.sex
                        )
                    ).await()
                Resource.Success(updatingResult)
            }
        }

    }

    suspend fun getListOfUserMaps(): List<UserMap> {
        return withContext(Dispatchers.IO) {
            val listOfUserMaps = cloud.collection("userLocations")
                .get().await().toObjects(UserMap::class.java)
            listOfUserMaps
        }
    }

    suspend fun getListOfUserEmails(): List<String> {
        return withContext(Dispatchers.IO) {
            val listOfUserEmails = mutableListOf<String>()
            val listOfListOfUserTasks = mutableListOf<List<UserTask>>()
            cloud.collection("userTasks")
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        listOfUserEmails.add(document.id)
                    }
                }.await()
            listOfUserEmails
        }
    }

    suspend fun addUserTask(userTask: UserTask, dateWithTime: String) {
        withContext(Dispatchers.IO) {
            cloud.collection("userTasks")
                .document(userTask.user.email!!)
                .get().addOnCompleteListener {
                    if (!it.result.exists()) {
                        cloud.collection("userTasks")
                            .document(userTask.user.email)
                            .set(mapOf(("first" to "first")))
                        cloud.collection("userTasks")
                            .document(userTask.user.email)
                            .collection("tasks")
                            .document(dateWithTime)
                            .set(userTask)
                    } else {
                        cloud.collection("userTasks")
                            .document(userTask.user.email)
                            .collection("tasks")
                            .document(dateWithTime)
                            .set(userTask)
                    }
                }.await()
        }
    }

    suspend fun getUserTasks(email: String): List<UserTask> {
        return withContext(Dispatchers.IO) {
            cloud.collection("userTasks")
                .document(email)
                .collection("tasks")
                .get().await().toObjects(UserTask::class.java)
        }
    }

    suspend fun deleteUserTask(userTask: UserTask) {
        return withContext(Dispatchers.IO) {
            cloud.collection("userTasks")
                .document(userTask.user.email!!)
                .collection("tasks")
                .document(userTask.date)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }
    }

    suspend fun setUserTaskToCompleted(userTask: UserTask) {
        return withContext(Dispatchers.IO) {
            cloud.collection("userTasks")
                .document(userTask.user.email!!)
                .collection("tasks")
                .document(userTask.date)
                .update(mapOf("finished" to true))
                .await()
        }
    }

    suspend fun updateUserTask(userTask: UserTask, volunteer: User?) {
        return withContext(Dispatchers.IO) {
            cloud.collection("userTasks")
                .document(userTask.user.email!!)
                .collection("tasks")
                .document(userTask.date)
                .update(mapOf("volunteer" to volunteer))
                .await()
        }
    }

    suspend fun updateUserDailySteps(user: User, dailySteps: List<DailySteps?>) {

        cloud.collection("users")
            .document(user.uid!!)
            .update(mapOf("dailySteps" to dailySteps))
            .await()

    }

    suspend fun addMedicamentsToFirebase(user: User, medicaments: List<Medicament>) {
        withContext(Dispatchers.IO){
            cloud.collection("users")
                .document(user.uid!!)
                .update(mapOf("medicaments" to medicaments))
                .await()
        }
    }

}