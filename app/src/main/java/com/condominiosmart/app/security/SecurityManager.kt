package com.condominiosmart.app.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import java.util.*

class SecurityManager(private val context: Context) {
    
    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val KEY_ALIAS = "CondominioSmartKey"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16
    }
    
    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
    
    init {
        keyStore.load(null)
        createKeyIfNeeded()
    }
    
    private fun createKeyIfNeeded() {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE_PROVIDER
            )
            
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(false)
                .setRandomizedEncryptionRequired(true)
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }
    
    fun encryptData(data: String): String {
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
        val cipher = Cipher.getInstance(TRANSFORMATION)
        
        // Genera IV casuale
        val iv = ByteArray(GCM_IV_LENGTH)
        Random().nextBytes(iv)
        
        val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec)
        
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        
        // Combina IV + dati crittografati
        val combined = iv + encryptedData
        return Base64.getEncoder().encodeToString(combined)
    }
    
    fun decryptData(encryptedData: String): String {
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
        val cipher = Cipher.getInstance(TRANSFORMATION)
        
        val combined = Base64.getDecoder().decode(encryptedData)
        
        // Estrai IV e dati crittografati
        val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
        val encryptedBytes = combined.copyOfRange(GCM_IV_LENGTH, combined.size)
        
        val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
        
        val decryptedData = cipher.doFinal(encryptedBytes)
        return String(decryptedData, Charsets.UTF_8)
    }
    
    fun hashData(data: String, salt: String): String {
        val combined = data + salt
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(combined.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(hash)
    }
    
    fun generateSalt(): String {
        val salt = ByteArray(32)
        Random().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }
    
    fun isDataEncrypted(data: String): Boolean {
        return try {
            Base64.getDecoder().decode(data)
            true
        } catch (e: Exception) {
            false
        }
    }
} 