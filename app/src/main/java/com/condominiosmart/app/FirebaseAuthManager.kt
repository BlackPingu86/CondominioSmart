package com.condominiosmart.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                // Crea un documento utente nel database
                val userData = hashMapOf(
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )
                db.collection("users").document(user.uid).set(userData).await()
                Result.success(user)
            } ?: Result.failure(Exception("Registrazione fallita"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                Result.success(user)
            } ?: Result.failure(Exception("Login fallito"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
} 