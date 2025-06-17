package com.condominiosmart.app.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricManager(private val context: Context) {
    
    private val biometricManager = BiometricManager.from(context)
    
    fun isBiometricAvailable(): Boolean {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
    }
    
    fun showBiometricPrompt(
        activity: FragmentActivity,
        title: String = "Autenticazione Sicura",
        subtitle: String = "Usa il tuo impronta digitale o volto per accedere",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            })
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Annulla")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    fun getBiometricType(): String {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> "Disponibile"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "Non disponibile"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Hardware non disponibile"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "Non configurato"
            else -> "Sconosciuto"
        }
    }
    
    fun isFingerprintAvailable(): Boolean {
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.FINGERPRINT) == BiometricManager.BIOMETRIC_SUCCESS
    }
    
    fun isFaceAvailable(): Boolean {
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.FACE) == BiometricManager.BIOMETRIC_SUCCESS
    }
} 