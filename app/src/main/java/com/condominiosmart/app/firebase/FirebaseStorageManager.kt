package com.condominiosmart.app.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class FirebaseStorageManager {

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    /**
     * Carica un file su Firebase Storage.
     * @param fileUri L'URI del file da caricare.
     * @param path Il percorso nel bucket di Storage dove salvare il file (es. "documenti/nome_file.pdf").
     * @return L'URL di download del file caricato.
     */
    suspend fun uploadFile(fileUri: Uri, path: String): Uri {
        val fileRef: StorageReference = storageRef.child(path)
        val uploadTask = fileRef.putFile(fileUri)
        val taskSnapshot = uploadTask.await()
        return taskSnapshot.metadata?.reference?.downloadUrl?.await() ?: throw Exception("Download URL non disponibile")
    }

    /**
     * Elimina un file da Firebase Storage.
     * @param url L'URL di download del file da eliminare.
     */
    suspend fun deleteFile(url: String) {
        val fileRef = storage.getReferenceFromUrl(url)
        fileRef.delete().await()
    }
} 