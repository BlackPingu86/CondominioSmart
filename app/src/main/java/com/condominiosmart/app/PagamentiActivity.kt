package com.condominiosmart.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.condominiosmart.app.adapters.PagamentiAdapter
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.condominiosmart.app.models.Pagamento
import com.condominiosmart.app.models.TipoPagamento
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class PagamentiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var textNoPagamenti: TextView
    private lateinit var adapter: PagamentiAdapter
    private lateinit var databaseManager: FirebaseDatabaseManager
    
    // Filtri e ricerca
    private lateinit var editSearch: TextInputEditText
    private lateinit var chipGroupStato: ChipGroup
    private lateinit var chipGroupTipo: ChipGroup
    
    private var allPagamenti: List<Pagamento> = emptyList()
    private var filteredPagamenti: List<Pagamento> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamenti)

        // Animazione zoom in per entrata
        animateEntrance()

        databaseManager = FirebaseDatabaseManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { 
            // Animazione zoom out per uscita
            animateExit()
        }

        // Inizializza le view
        recyclerView = findViewById(R.id.recyclerView)
        textNoPagamenti = findViewById(R.id.textNoPagamenti)
        editSearch = findViewById(R.id.editSearch)
        chipGroupStato = findViewById(R.id.chipGroupStato)
        chipGroupTipo = findViewById(R.id.chipGroupTipo)

        // Setup del RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PagamentiAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        // Setup del FAB con animazione
        findViewById<FloatingActionButton>(R.id.fabNuovoPagamento).setOnClickListener {
            startActivityWithZoomIn(Intent(this, NuovoPagamentoActivity::class.java))
        }

        // Setup dei filtri
        setupFilters()

        // Carica i pagamenti
        loadPagamenti()
    }

    private fun animateEntrance() {
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        findViewById<View>(android.R.id.content).startAnimation(zoomIn)
    }

    private fun animateExit() {
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        findViewById<View>(android.R.id.content).startAnimation(zoomOut)
        
        zoomOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                onBackPressed()
            }
        })
    }

    private fun startActivityWithZoomIn(intent: Intent) {
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        findViewById<View>(android.R.id.content).startAnimation(zoomOut)
        
        zoomOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                startActivity(intent)
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadPagamenti()
    }

    private fun setupFilters() {
        // Listener per la ricerca
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
        })

        // Listener per il filtro stato
        chipGroupStato.setOnCheckedChangeListener { _, checkedId ->
            applyFilters()
        }

        // Listener per il filtro tipo
        chipGroupTipo.setOnCheckedChangeListener { _, checkedId ->
            applyFilters()
        }
    }

    private fun loadPagamenti() {
        // Mostra indicatore di caricamento
        val loadingSnackbar = showLoadingSnackbar("Caricamento pagamenti...")
        
        lifecycleScope.launch {
            try {
                allPagamenti = databaseManager.getPagamenti()
                loadingSnackbar.dismiss()
                applyFilters()
                
                // Mostra feedback positivo se ci sono pagamenti
                if (allPagamenti.isNotEmpty()) {
                    showSuccessSnackbar("${allPagamenti.size} pagamento${if (allPagamenti.size > 1) "i" else ""} caricato${if (allPagamenti.size > 1) "i" else ""}")
                }
            } catch (e: Exception) {
                loadingSnackbar.dismiss()
                showErrorSnackbar("Errore nel caricamento dei pagamenti: ${e.message}")
                updateVisibility(true)
            }
        }
    }

    private fun applyFilters() {
        val searchQuery = editSearch.text?.toString()?.trim()?.lowercase() ?: ""
        val selectedStatoChip = findViewById<Chip>(chipGroupStato.checkedChipId)
        val selectedTipoChip = findViewById<Chip>(chipGroupTipo.checkedChipId)

        filteredPagamenti = allPagamenti.filter { pagamento ->
            // Filtro per ricerca
            val matchesSearch = searchQuery.isEmpty() || 
                pagamento.titolo.lowercase().contains(searchQuery) ||
                pagamento.descrizione.lowercase().contains(searchQuery)

            // Filtro per stato
            val matchesStato = when (selectedStatoChip?.id) {
                R.id.chipTutti -> true
                R.id.chipDaPagare -> !pagamento.pagato
                R.id.chipPagati -> pagamento.pagato
                else -> true
            }

            // Filtro per tipo
            val matchesTipo = when (selectedTipoChip?.id) {
                R.id.chipTipoTutti -> true
                R.id.chipFattura -> pagamento.tipoPagamento == TipoPagamento.FATTURA
                R.id.chipBolletta -> pagamento.tipoPagamento == TipoPagamento.BOLLETTA
                R.id.chipRicevuta -> pagamento.tipoPagamento == TipoPagamento.RICEVUTA
                R.id.chipAltro -> pagamento.tipoPagamento == TipoPagamento.ALTRO
                else -> true
            }

            matchesSearch && matchesStato && matchesTipo
        }

        adapter.updatePagamenti(filteredPagamenti)
        updateVisibility(filteredPagamenti.isEmpty())
        
        // Mostra feedback sui risultati dei filtri
        showFilterResults()
    }

    private fun showFilterResults() {
        val searchQuery = editSearch.text?.toString()?.trim()
        val hasFilters = !searchQuery.isNullOrEmpty() || 
                        chipGroupStato.checkedChipId != R.id.chipTutti ||
                        chipGroupTipo.checkedChipId != R.id.chipTipoTutti
        
        if (hasFilters && filteredPagamenti.isNotEmpty()) {
            val message = when {
                !searchQuery.isNullOrEmpty() -> "Trovati ${filteredPagamenti.size} risultato${if (filteredPagamenti.size > 1) "i" else ""}"
                else -> "Filtro applicato: ${filteredPagamenti.size} pagamento${if (filteredPagamenti.size > 1) "i" else ""}"
            }
            showSuccessSnackbar(message)
        }
    }

    private fun updateVisibility(isEmpty: Boolean) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        textNoPagamenti.visibility = if (isEmpty) View.VISIBLE else View.GONE
        
        // Aggiorna il testo in base ai filtri applicati
        if (isEmpty) {
            val searchQuery = editSearch.text?.toString()?.trim()
            if (!searchQuery.isNullOrEmpty()) {
                textNoPagamenti.text = "Nessun pagamento trovato per \"$searchQuery\""
            } else {
                textNoPagamenti.text = "Nessun pagamento disponibile"
            }
        }
    }

    private fun showLoadingSnackbar(message: String): Snackbar {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_INDEFINITE)
        val snackbarView = snackbar.view
        val snackbarLayout = snackbarView as Snackbar.SnackbarLayout
        
        val customView = layoutInflater.inflate(R.layout.snackbar_loading, null)
        customView.findViewById<TextView>(R.id.textLoadingMessage).text = message
        snackbarLayout.addView(customView, 0)
        
        snackbar.show()
        return snackbar
    }

    private fun showSuccessSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
} 