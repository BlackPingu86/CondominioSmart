package com.condominiosmart.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Animazione di entrata con zoom in
        animateEntrance()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup del FloatingActionButton con animazione
        val fabMenu = findViewById<FloatingActionButton>(R.id.fabMenu)
        fabMenu.setOnClickListener {
            animateFabClick(fabMenu)
            showMenu()
        }

        // Setup delle card con animazioni
        setupCards()
    }

    private fun animateEntrance() {
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        
        // Animazione zoom in per l'intero contenuto
        findViewById<View>(android.R.id.content).startAnimation(zoomIn)
        
        // Animazione sequenziale delle card con delay
        val cards = listOf(
            findViewById<CardView>(R.id.cardAvvisi),
            findViewById<CardView>(R.id.cardPagamenti),
            findViewById<CardView>(R.id.cardAssemblee),
            findViewById<CardView>(R.id.cardDocumenti)
        )
        
        cards.forEachIndexed { index, card ->
            card.alpha = 0f
            card.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay((index * 150).toLong())
                .start()
        }
    }

    private fun animateFabClick(fab: FloatingActionButton) {
        fab.animate()
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(100)
            .withEndAction {
                fab.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun setupCards() {
        // Card Avvisi con animazione hover
        val cardAvvisi = findViewById<CardView>(R.id.cardAvvisi)
        val textAvvisi = findViewById<TextView>(R.id.textAvvisi)
        
        cardAvvisi.setOnClickListener {
            animateCardClick(cardAvvisi) {
                startActivityWithZoomIn(Intent(this, AvvisiActivity::class.java))
            }
        }

        // Card Pagamenti con animazione hover
        val cardPagamenti = findViewById<CardView>(R.id.cardPagamenti)
        val textPagamenti = findViewById<TextView>(R.id.textPagamenti)
        
        cardPagamenti.setOnClickListener {
            animateCardClick(cardPagamenti) {
                startActivityWithZoomIn(Intent(this, PagamentiActivity::class.java))
            }
        }

        // Card Assemblee con animazione hover
        val cardAssemblee = findViewById<CardView>(R.id.cardAssemblee)
        val textAssemblee = findViewById<TextView>(R.id.textAssemblee)
        
        cardAssemblee.setOnClickListener {
            animateCardClick(cardAssemblee) {
                showFeatureComingSoon("Assemblee")
            }
        }

        // Card Documenti con animazione hover
        val cardDocumenti = findViewById<CardView>(R.id.cardDocumenti)
        val textDocumenti = findViewById<TextView>(R.id.textDocumenti)
        
        cardDocumenti.setOnClickListener {
            animateCardClick(cardDocumenti) {
                startActivityWithZoomIn(Intent(this, DocumentiActivity::class.java))
            }
        }

        // Aggiungi animazioni hover per tutte le card
        setupCardHoverAnimations(cardAvvisi, cardPagamenti, cardAssemblee, cardDocumenti)
    }

    private fun startActivityWithZoomIn(intent: Intent) {
        // Animazione zoom out per uscita
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        findViewById<View>(android.R.id.content).startAnimation(zoomOut)
        
        zoomOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                startActivity(intent)
                // Aggiungi animazione zoom in per la nuova activity
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            }
        })
    }

    private fun animateCardClick(card: CardView, onComplete: () -> Unit) {
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        card.startAnimation(bounce)
        
        bounce.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                onComplete()
            }
        })
    }

    private fun setupCardHoverAnimations(vararg cards: CardView) {
        cards.forEach { card ->
            card.setOnHoverListener { _, event ->
                when (event.action) {
                    android.view.MotionEvent.ACTION_HOVER_ENTER -> {
                        card.animate()
                            .translationZ(16f)
                            .setDuration(200)
                            .start()
                    }
                    android.view.MotionEvent.ACTION_HOVER_EXIT -> {
                        card.animate()
                            .translationZ(0f)
                            .setDuration(200)
                            .start()
                    }
                }
                true
            }
        }
    }

    private fun showMenu() {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "Menu opzioni in arrivo!",
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction("OK") { snackbar.dismiss() }
        snackbar.show()
    }

    private fun showFeatureComingSoon(feature: String) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "Funzionalità $feature in arrivo!",
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction("OK") { snackbar.dismiss() }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                showFeatureComingSoon("Profilo")
                true
            }
            R.id.action_theme -> {
                toggleTheme()
                true
            }
            R.id.action_logout -> {
                // Animazione zoom out per logout
                val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
                findViewById<View>(android.R.id.content).startAnimation(zoomOut)
                
                zoomOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                    override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                    override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                    override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                        startActivity(Intent(this@HomeActivity, MainActivity::class.java))
                        finish()
                    }
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleTheme() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val newMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        
        AppCompatDelegate.setDefaultNightMode(newMode)
        
        val themeName = if (newMode == AppCompatDelegate.MODE_NIGHT_YES) "Scuro" else "Chiaro"
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "Tema cambiato in $themeName",
            Snackbar.LENGTH_SHORT
        )
        snackbar.show()
    }
} 