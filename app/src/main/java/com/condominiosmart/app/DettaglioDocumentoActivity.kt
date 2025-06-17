package com.condominiosmart.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.condominiosmart.app.firebase.FirebaseStorageManager
import com.condominiosmart.app.models.Documento
import com.condominiosmart.app.models.UserRole
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DettaglioDocumentoActivity : AppCompatActivity() {
    private lateinit var databaseManager: FirebaseDatabaseManager
    private lateinit var storageManager: FirebaseStorageManager
    private var documento: Documento? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dettaglio_documento)

        databaseManager = FirebaseDatabaseManager()
        storageManager = FirebaseStorageManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Recupera il documento dall'intent
        documento = intent.getSerializableExtra("documento") as? Documento
        if (documento == null) {
            Snackbar.make(findViewById(android.R.id.content), "Errore nel caricamento del documento", Snackbar.LENGTH_SHORT).show()
            finish()
            return
        }

        // Popola i campi
        findViewById<TextView>(R.id.textTitolo).text = documento?.titolo
        findViewById<TextView>(R.id.textTipo).text = documento?.tipo?.name
        findViewById<TextView>(R.id.textData).text = dateFormat.format(documento?.dataCaricamento)
        findViewById<TextView>(R.id.textAutore).text = "Caricato da: ${documento?.autore}"
        findViewById<TextView>(R.id.textDescrizione).text = documento?.descrizione

        // Setup del pulsante per aprire il documento
        findViewById<Button>(R.id.buttonApriDocumento).setOnClickListener {
            documento?.url?.let { url ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (e: Exception) {
                    Snackbar.make(findViewById(android.R.id.content), "Impossibile aprire il documento: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        // Setup del FAB per l'eliminazione
        val fabEliminaDocumento = findViewById<FloatingActionButton>(R.id.fabEliminaDocumento)
        fabEliminaDocumento.visibility = View.GONE // Nascondi inizialmente

        // Verifica se l'utente è amministratore
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                try {
                    val userRole = databaseManager.getUserRole(currentUser.uid)
                    if (userRole == UserRole.ADMIN) {
                        fabEliminaDocumento.visibility = View.VISIBLE
                        fabEliminaDocumento.setOnClickListener {
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
        AlertDialog.Builder(this)
            .setTitle("Elimina Documento")
            .setMessage("Sei sicuro di voler eliminare questo documento?")
            .setPositiveButton("Elimina") { _, _ ->
                deleteDocumento()
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun deleteDocumento() {
        val documentoId = documento?.id ?: return
        val documentoUrl = documento?.url ?: return
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        lifecycleScope.launch {
            try {
                // Prima elimina il file da Firebase Storage
                storageManager.deleteFile(documentoUrl)

                // Poi elimina il documento da Firestore
                databaseManager.deleteDocumento(documentoId, currentUser.uid).fold(
                    onSuccess = {
                        Snackbar.make(findViewById(android.R.id.content), "Documento eliminato con successo", Snackbar.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = { exception ->
                        Snackbar.make(findViewById(android.R.id.content), "Errore: ${exception.message}", Snackbar.LENGTH_SHORT).show()
                    }
                )
            } catch (e: Exception) {
                Snackbar.make(findViewById(android.R.id.content), "Errore nell'eliminazione del documento: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
} 