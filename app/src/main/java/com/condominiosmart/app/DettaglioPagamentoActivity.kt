package com.condominiosmart.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.condominiosmart.app.firebase.FirebaseStorageManager
import com.condominiosmart.app.models.Pagamento
import com.condominiosmart.app.models.UserRole
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class DettaglioPagamentoActivity : AppCompatActivity() {

    private lateinit var databaseManager: FirebaseDatabaseManager
    private lateinit var storageManager: FirebaseStorageManager
    private var pagamento: Pagamento? = null

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dettaglio_pagamento)

        databaseManager = FirebaseDatabaseManager()
        storageManager = FirebaseStorageManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Recupera il pagamento dall'intent
        pagamento = intent.getSerializableExtra("pagamento") as? Pagamento
        if (pagamento == null) {
            Snackbar.make(findViewById(android.R.id.content), "Errore nel caricamento del pagamento", Snackbar.LENGTH_SHORT).show()
            finish()
            return
        }

        // Popola i campi
        findViewById<TextView>(R.id.textTitolo).text = pagamento?.titolo
        findViewById<TextView>(R.id.textImporto).text = currencyFormat.format(pagamento?.importo)
        findViewById<TextView>(R.id.textTipo).text = pagamento?.tipoPagamento?.name

        pagamento?.dataScadenza?.let { 
            findViewById<TextView>(R.id.textDataScadenza).text = "Scadenza: ${dateFormat.format(it)}"
            findViewById<TextView>(R.id.textDataScadenza).visibility = View.VISIBLE
        } ?: run { 
            findViewById<TextView>(R.id.textDataScadenza).visibility = View.GONE
        }

        pagamento?.dataPagamento?.let {
            findViewById<TextView>(R.id.textDataPagamento).text = "Pagato il: ${dateFormat.format(it)}"
            findViewById<TextView>(R.id.textDataPagamento).visibility = View.VISIBLE
        } ?: run {
            findViewById<TextView>(R.id.textDataPagamento).visibility = View.GONE
        }
        
        findViewById<TextView>(R.id.textAutore).text = "Caricato da: ${pagamento?.autore}"
        findViewById<TextView>(R.id.textDescrizione).text = pagamento?.descrizione

        val textStatoPagamento = findViewById<TextView>(R.id.textStatoPagamento)
        val iconStatoPagamento = findViewById<ImageView>(R.id.iconStatoPagamento)

        if (pagamento?.pagato == true) {
            textStatoPagamento.text = "Pagato"
            textStatoPagamento.setTextColor(ContextCompat.getColor(this, R.color.green_paid))
            iconStatoPagamento.setImageResource(R.drawable.ic_check_circle)
            iconStatoPagamento.setColorFilter(ContextCompat.getColor(this, R.color.green_paid))
        } else {
            textStatoPagamento.text = "Da pagare"
            textStatoPagamento.setTextColor(ContextCompat.getColor(this, R.color.red_unpaid))
            iconStatoPagamento.setImageResource(R.drawable.ic_warning)
            iconStatoPagamento.setColorFilter(ContextCompat.getColor(this, R.color.red_unpaid))
        }

        // Setup del pulsante per aprire il file
        findViewById<Button>(R.id.buttonApriFile).setOnClickListener {
            pagamento?.urlFile?.let { url ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (e: Exception) {
                    Snackbar.make(findViewById(android.R.id.content), "Impossibile aprire il file: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        // Setup del FAB per l'eliminazione
        val fabEliminaPagamento = findViewById<FloatingActionButton>(R.id.fabEliminaPagamento)
        fabEliminaPagamento.visibility = View.GONE // Nascondi inizialmente

        // Verifica se l'utente è amministratore
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                try {
                    val userRole = databaseManager.getUserRole(currentUser.uid)
                    if (userRole == UserRole.ADMIN) {
                        fabEliminaPagamento.visibility = View.VISIBLE
                        fabEliminaPagamento.setOnClickListener {
                            showDeleteConfirmationDialog()
                        }
                    }
                } catch (e: Exception) {
                    Snackbar.make(findViewById(android.R.id.content), "Errore nel controllo dei permessi: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_delete, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Popola i dettagli del pagamento nel dialog
        val textPagamentoTitolo = dialogView.findViewById<TextView>(R.id.textPagamentoTitolo)
        val textPagamentoImporto = dialogView.findViewById<TextView>(R.id.textPagamentoImporto)
        val textPagamentoTipo = dialogView.findViewById<TextView>(R.id.textPagamentoTipo)

        textPagamentoTitolo.text = pagamento?.titolo
        textPagamentoImporto.text = currencyFormat.format(pagamento?.importo)
        textPagamentoTipo.text = pagamento?.tipoPagamento?.name

        // Setup dei pulsanti
        dialogView.findViewById<Button>(R.id.buttonAnnulla).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonElimina).setOnClickListener {
            // Animazione di vibrazione per feedback tattile
            it.animate()
                .translationX(10f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .translationX(-10f)
                        .setDuration(100)
                        .withEndAction {
                            it.animate()
                                .translationX(0f)
                                .setDuration(100)
                                .withEndAction {
                                    dialog.dismiss()
                                    deletePagamento()
                                }
                        }
                }
        }

        dialog.show()
    }

    private fun deletePagamento() {
        val pagamentoId = pagamento?.id ?: return
        val pagamentoUrl = pagamento?.urlFile ?: return
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        // Mostra un indicatore di caricamento
        val loadingSnackbar = Snackbar.make(findViewById(android.R.id.content), "Eliminazione in corso...", Snackbar.LENGTH_INDEFINITE)
        loadingSnackbar.show()

        lifecycleScope.launch {
            try {
                // Prima elimina il file da Firebase Storage
                if (pagamentoUrl.isNotEmpty()) {
                    storageManager.deleteFile(pagamentoUrl)
                }

                // Poi elimina il pagamento da Firestore
                databaseManager.deletePagamento(pagamentoId, currentUser.uid).fold(
                    onSuccess = {
                        loadingSnackbar.dismiss()
                        
                        // Mostra Snackbar di successo personalizzato
                        val successSnackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG)
                        val snackbarView = successSnackbar.view
                        val snackbarLayout = snackbarView as Snackbar.SnackbarLayout
                        
                        val customView = layoutInflater.inflate(R.layout.snackbar_success, null)
                        snackbarLayout.addView(customView, 0)
                        
                        successSnackbar.show()
                        
                        // Chiudi l'activity dopo un breve delay
                        successSnackbar.addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                finish()
                            }
                        })
                    },
                    onFailure = { exception ->
                        loadingSnackbar.dismiss()
                        Snackbar.make(findViewById(android.R.id.content), "Errore: ${exception.message}", Snackbar.LENGTH_LONG).show()
                    }
                )
            } catch (e: Exception) {
                loadingSnackbar.dismiss()
                Snackbar.make(findViewById(android.R.id.content), "Errore nell'eliminazione del pagamento: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
} 