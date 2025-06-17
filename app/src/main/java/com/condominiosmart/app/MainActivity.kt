package com.condominiosmart.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Animazione di entrata con zoom in
        animateEntrance()

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val registerButton = findViewById<Button>(R.id.buttonRegister)

        // Animazioni per i pulsanti
        setupButtonAnimations(loginButton, registerButton)

        loginButton.setOnClickListener {
            animateButtonClick(loginButton) {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    showSuccessMessage("Login effettuato!")
                    try {
                        // Animazione zoom out per transizione
                        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
                        findViewById<View>(android.R.id.content).startAnimation(zoomOut)
                        
                        zoomOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                // Aggiungi animazione zoom in per la HomeActivity
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                                finish()
                            }
                        })
                    } catch (e: Exception) {
                        showErrorMessage("Errore: ${e.message}")
                    }
                } else {
                    showErrorMessage("Inserisci email e password")
                    shakeView(findViewById(android.R.id.content))
                }
            }
        }

        registerButton.setOnClickListener {
            animateButtonClick(registerButton) {
                showInfoMessage("Registrazione in arrivo!")
            }
        }
    }

    private fun animateEntrance() {
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        
        // Animazione zoom in per l'intero contenuto
        findViewById<View>(android.R.id.content).startAnimation(zoomIn)
        
        // Animazione sequenziale dei campi con delay
        val views = listOf(
            findViewById<TextView>(R.id.textTitle),
            findViewById<EditText>(R.id.editTextEmail),
            findViewById<EditText>(R.id.editTextPassword),
            findViewById<Button>(R.id.buttonLogin),
            findViewById<Button>(R.id.buttonRegister)
        )
        
        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.animate()
                .alpha(1f)
                .setDuration(400)
                .setStartDelay((index * 200).toLong())
                .start()
        }
    }

    private fun setupButtonAnimations(vararg buttons: Button) {
        buttons.forEach { button ->
            button.setOnHoverListener { _, event ->
                when (event.action) {
                    android.view.MotionEvent.ACTION_HOVER_ENTER -> {
                        button.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(200)
                            .start()
                    }
                    android.view.MotionEvent.ACTION_HOVER_EXIT -> {
                        button.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start()
                    }
                }
                true
            }
        }
    }

    private fun animateButtonClick(button: Button, onComplete: () -> Unit) {
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        button.startAnimation(bounce)
        
        bounce.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                onComplete()
            }
        })
    }

    private fun shakeView(view: View) {
        val shake = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        shake.duration = 100
        view.startAnimation(shake)
    }

    private fun showSuccessMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showErrorMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun showInfoMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }
} 