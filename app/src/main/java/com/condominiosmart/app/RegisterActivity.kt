package com.condominiosmart.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var authManager: FirebaseAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        authManager = FirebaseAuthManager()

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmPassword)
        val registerButton = findViewById<Button>(R.id.buttonRegister)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (validateInput(email, password, confirmPassword)) {
                lifecycleScope.launch {
                    try {
                        authManager.registerUser(email, password).fold(
                            onSuccess = {
                                Toast.makeText(this@RegisterActivity, "Registrazione completata!", Toast.LENGTH_SHORT).show()
                                finish()
                            },
                            onFailure = { exception ->
                                Toast.makeText(this@RegisterActivity, "Errore: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } catch (e: Exception) {
                        Toast.makeText(this@RegisterActivity, "Errore: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Inserisci un'email valida", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty() || password.length < 6) {
            Toast.makeText(this, "La password deve essere di almeno 6 caratteri", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Le password non coincidono", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
} 