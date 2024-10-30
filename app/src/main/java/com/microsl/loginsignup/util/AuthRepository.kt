package com.microsl.loginsignup.util

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.microsl.loginsignup.Users
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository {

    private val tag = "AuthRepository"

    // Initialize Firebase Auth and Firebase Database
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun isLoggedIn(): Boolean {
        if (firebaseAuth.currentUser != null) {
            Log.d(tag, "Already logged in")
            return true
        }

        return false
    }

    suspend fun register(user: String, email: String, password: String): Boolean {
        try {
            val result = suspendCoroutine { continuation ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        Log.e(tag, "register success")
                        CoroutineScope(Dispatchers.IO).launch {
                            val id = task.result.user!!.uid
                            val userInfo = Users(id, user, email)
                            database.reference.child("Users").child(id).setValue(userInfo)

                            continuation.resume(login(email, password))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(false)
                        Log.e(tag, "register failure")
                    }
            }

            return result
        } catch (e: Exception) {
            Log.e(tag, e.printStackTrace().toString())
            if (e is CancellationException) throw e
            Log.e(tag, "register exception ${e.message}")
            return false
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        try {
            Log.d(tag, "Enter Try Block")
            val result = suspendCoroutine { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Log.e(tag, "Login Successful")
                        continuation.resume(true)
                    }
                    .addOnFailureListener {
                        Log.e(tag, "Login Failure")
                        continuation.resume(false)
                    }
            }
            return result
        } catch (e: Exception) {
            Log.d(tag, "Enter Catch Block")
            Log.e(tag, e.printStackTrace().toString())
            if (e is CancellationException) throw e
            Log.e(tag, "login exception ${e.message}")
            return false
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}