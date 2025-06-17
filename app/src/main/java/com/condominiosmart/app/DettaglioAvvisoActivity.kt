package com.condominiosmart.app

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.condominiosmart.app.models.Avviso
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DettaglioAvvisoActivity : AppCompatActivity() {
    private lateinit var databaseManager: FirebaseDatabaseManager
    private var avviso: Avviso? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dettaglio_avviso)

        databaseManager = FirebaseDatabaseManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Recupera l'avviso dall'intent
        avviso = intent.getSerializableExtra("avviso") as? Avviso
        if (avviso == null) {
            Snackbar.make(findViewById(android.R.id.content), "Errore nel caricamento dell'avviso", Snackbar.LENGTH_SHORT).show()
            finish()
            return
        }

        // Popola i campi
        findViewById<TextView>(R.id.textTitolo).text = avviso?.titolo
        findViewById<TextView>(R.id.textData).text = dateFormat.format(avviso?.data)
        findViewById<TextView>(R.id.textContenuto).text = avviso?.contenuto
        findViewById<TextView>(R.id.textAutore).text = "Pubblicato da: ${avviso?.autore}"

        // Setup del FAB per l'eliminazione
        val fabElimina = findViewById<FloatingActionButton>(R.id.fabElimina)
        fabElimina.visibility = View.GONE // Nascondi inizialmente

        // Verifica se l'utente è amministratore
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                try {
                    val userRole = databaseManager.getUserRole(currentUser.uid)
                    if (userRole == UserRole.ADMIN) {
                        fabElimina.visibility = View.VISIBLE
                        fabElimina.setOnClickListener {
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
            .setTitle("Elimina Avviso")
            .setMessage("Sei sicuro di voler eliminare questo avviso?")
            .setPositiveButton("Elimina") { _, _ ->
                deleteAvviso()
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun deleteAvviso() {
        val avvisoId = avviso?.id ?: return
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        lifecycleScope.launch {
            try {
                databaseManager.deleteAvviso(avvisoId, currentUser.uid).fold(
                    onSuccess = {
                        Snackbar.make(findViewById(android.R.id.content), "Avviso eliminato con successo", Snackbar.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = { exception ->
                        Snackbar.make(findViewById(android.R.id.content), "Errore: ${exception.message}", Snackbar.LENGTH_SHORT).show()
                    }
                )
            } catch (e: Exception) {
                Snackbar.make(findViewById(android.R.id.content), "Errore: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
} 