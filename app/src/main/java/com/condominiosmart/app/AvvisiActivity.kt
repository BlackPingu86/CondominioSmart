package com.condominiosmart.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.condominiosmart.app.adapters.AvvisiAdapter
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AvvisiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var textNoAvvisi: TextView
    private lateinit var adapter: AvvisiAdapter
    private lateinit var databaseManager: FirebaseDatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avvisi)

        databaseManager = FirebaseDatabaseManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Inizializza le view
        recyclerView = findViewById(R.id.recyclerView)
        textNoAvvisi = findViewById(R.id.textNoAvvisi)

        // Setup del RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AvvisiAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        // Setup del FAB
        findViewById<FloatingActionButton>(R.id.fabNuovoAvviso).setOnClickListener {
            startActivity(Intent(this, NuovoAvvisoActivity::class.java))
        }

        // Carica gli avvisi
        loadAvvisi()
    }

    override fun onResume() {
        super.onResume()
        loadAvvisi()
    }

    private fun loadAvvisi() {
        lifecycleScope.launch {
            try {
                val avvisi = databaseManager.getAvvisi()
                adapter.updateAvvisi(avvisi)
                updateVisibility(avvisi.isEmpty())
            } catch (e: Exception) {
                Snackbar.make(findViewById(android.R.id.content), "Errore nel caricamento degli avvisi: ${e.message}", Snackbar.LENGTH_SHORT).show()
                updateVisibility(true)
            }
        }
    }

    private fun updateVisibility(isEmpty: Boolean) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        textNoAvvisi.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
} 