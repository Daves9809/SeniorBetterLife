package com.example.seniorbetterlife.profile.util

inline fun<T> safeCall(action: () -> Resource<T>): Resource<T>{
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}