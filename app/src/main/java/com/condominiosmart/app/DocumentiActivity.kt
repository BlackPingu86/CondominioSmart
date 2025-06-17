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
import com.condominiosmart.app.adapters.DocumentiAdapter
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DocumentiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var textNoDocumenti: TextView
    private lateinit var adapter: DocumentiAdapter
    private lateinit var databaseManager: FirebaseDatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documenti)

        databaseManager = FirebaseDatabaseManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Inizializza le view
        recyclerView = findViewById(R.id.recyclerView)
        textNoDocumenti = findViewById(R.id.textNoDocumenti)

        // Setup del RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DocumentiAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        // Setup del FAB
        findViewById<FloatingActionButton>(R.id.fabNuovoDocumento).setOnClickListener {
            startActivity(Intent(this, NuovoDocumentoActivity::class.java))
        }

        // Carica i documenti
        loadDocumenti()
    }

    override fun onResume() {
        super.onResume()
        loadDocumenti()
    }

    private fun loadDocumenti() {
        lifecycleScope.launch {
            try {
                val documenti = databaseManager.getDocumenti()
                adapter.updateDocumenti(documenti)
                updateVisibility(documenti.isEmpty())
            } catch (e: Exception) {
                Snackbar.make(findViewById(android.R.id.content), "Errore nel caricamento dei documenti: ${e.message}", Snackbar.LENGTH_SHORT).show()
                updateVisibility(true)
            }
        }
    }

    private fun updateVisibility(isEmpty: Boolean) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        textNoDocumenti.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
} 