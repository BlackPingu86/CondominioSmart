package com.condominiosmart.app.security

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import java.util.*

class SecureStorage(private val context: Context) {
    
    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val KEY_ALIAS = "SecureStorageKey"
        private const val PREFS_NAME = "SecurePrefs"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16
    }
    
    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
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
    
    fun storeSecureData(key: String, value: String) {
        val encryptedValue = encryptData(value)
        sharedPreferences.edit().putString(key, encryptedValue).apply()
    }
    
    fun retrieveSecureData(key: String): String? {
        val encryptedValue = sharedPreferences.getString(key, null) ?: return null
        return try {
            decryptData(encryptedValue)
        } catch (e: Exception) {
            null
        }
    }
    
    fun removeSecureData(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
    
    fun clearAllSecureData() {
        sharedPreferences.edit().clear().apply()
    }
    
    fun containsKey(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
    
    private fun encryptData(data: String): String {
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
    
    private fun decryptData(encryptedData: String): String {
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
    
    // Metodi per dati specifici
    fun storeUserEmail(email: String) {
        storeSecureData("user_email", email)
    }
    
    fun getUserEmail(): String? {
        return retrieveSecureData("user_email")
    }
    
    fun storeUserName(name: String) {
        storeSecureData("user_name", name)
    }
    
    fun getUserName(): String? {
        return retrieveSecureData("user_name")
    }
    
    fun storeUserPhone(phone: String) {
        storeSecureData("user_phone", phone)
    }
    
    fun getUserPhone(): String? {
        return retrieveSecureData("user_phone")
    }
    
    fun storeUserAddress(address: String) {
        storeSecureData("user_address", address)
    }
    
    fun getUserAddress(): String? {
        return retrieveSecureData("user_address")
    }
    
    fun storeAuthToken(token: String) {
        storeSecureData("auth_token", token)
    }
    
    fun getAuthToken(): String? {
        return retrieveSecureData("auth_token")
    }
    
    fun clearAuthData() {
        removeSecureData("auth_token")
        removeSecureData("user_email")
        removeSecureData("user_name")
        removeSecureData("user_phone")
        removeSecureData("user_address")
    }
} 