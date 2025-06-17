package com.condominiosmart.app.firebase

import com.condominiosmart.app.models.Avviso
import com.condominiosmart.app.models.Documento
import com.condominiosmart.app.models.Pagamento
import com.condominiosmart.app.models.UserRole
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirebaseDatabaseManager {
    private val db = FirebaseFirestore.getInstance()
    private val avvisiCollection = db.collection("avvisi")
    private val usersCollection = db.collection("users")
    private val documentiCollection = db.collection("documenti")
    private val pagamentiCollection = db.collection("pagamenti")

    suspend fun getUserRole(userId: String): UserRole {
        return try {
            val userDoc = usersCollection.document(userId).get().await()
            val role = userDoc.getString("role") ?: "USER"
            UserRole.valueOf(role)
        } catch (e: Exception) {
            UserRole.USER
        }
    }

    suspend fun getAvvisi(): List<Avviso> {
        return try {
            val currentDate = Date()
            val avvisi = avvisiCollection
                .whereGreaterThanOrEqualTo("dataScadenza", currentDate)
                .orderBy("dataScadenza", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(Avviso::class.java)

            // Elimina automaticamente gli avvisi scaduti
            avvisiCollection
                .whereLessThan("dataScadenza", currentDate)
                .get()
                .await()
                .documents
                .forEach { doc ->
                    doc.reference.delete()
                }

            avvisi
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addAvviso(avviso: Avviso): Result<String> {
        return try {
            val docRef = avvisiCollection.add(avviso).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAvviso(avvisoId: String, userId: String): Result<Unit> {
        return try {
            val userRole = getUserRole(userId)
            if (userRole != UserRole.ADMIN) {
                return Result.failure(Exception("Solo gli amministratori possono eliminare gli avvisi"))
            }
            
            avvisiCollection.document(avvisoId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDocumenti(): List<Documento> {
        return try {
            documentiCollection
                .whereEqualTo("visibile", true)
                .orderBy("dataCaricamento", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(Documento::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addDocumento(documento: Documento): Result<String> {
        return try {
            val docRef = documentiCollection.add(documento).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDocumento(documentoId: String, userId: String): Result<Unit> {
        return try {
            val userRole = getUserRole(userId)
            if (userRole != UserRole.ADMIN) {
                return Result.failure(Exception("Solo gli amministratori possono eliminare i documenti"))
            }
            
            documentiCollection.document(documentoId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPagamenti(): List<Pagamento> {
        return try {
            pagamentiCollection
                .whereEqualTo("visibile", true)
                .orderBy("dataScadenza", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Pagamento::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addPagamento(pagamento: Pagamento): Result<String> {
        return try {
            val docRef = pagamentiCollection.add(pagamento).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePagamento(pagamentoId: String, userId: String): Result<Unit> {
        return try {
            val userRole = getUserRole(userId)
            if (userRole != UserRole.ADMIN) {
                return Result.failure(Exception("Solo gli amministratori possono eliminare i pagamenti"))
            }
            
            pagamentiCollection.document(pagamentoId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 