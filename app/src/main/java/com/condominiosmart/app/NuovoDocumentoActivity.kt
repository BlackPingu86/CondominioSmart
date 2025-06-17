package com.condominiosmart.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.condominiosmart.app.firebase.FirebaseStorageManager
import com.condominiosmart.app.models.Documento
import com.condominiosmart.app.models.TipoDocumento
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Date

class NuovoDocumentoActivity : AppCompatActivity() {

    private lateinit var databaseManager: FirebaseDatabaseManager
    private lateinit var storageManager: FirebaseStorageManager

    private lateinit var editTitolo: TextInputEditText
    private lateinit var editDescrizione: TextInputEditText
    private lateinit var textSelectType: TextView
    private lateinit var textSelectedFileName: TextView
    private lateinit var buttonSelectFile: android.widget.Button
    private lateinit var switchVisible: SwitchMaterial
    private lateinit var fabUploadDocumento: FloatingActionButton

    private var selectedFileUri: Uri? = null
    private var selectedTipoDocumento: TipoDocumento = TipoDocumento.ALTRO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuovo_documento)

        databaseManager = FirebaseDatabaseManager()
        storageManager = FirebaseStorageManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Inizializza le view
        editTitolo = findViewById(R.id.editTitolo)
        editDescrizione = findViewById(R.id.editDescrizione)
        textSelectType = findViewById(R.id.textSelectType)
        textSelectedFileName = findViewById(R.id.textSelectedFileName)
        buttonSelectFile = findViewById(R.id.buttonSelectFile)
        switchVisible = findViewById(R.id.switchVisible)
        fabUploadDocumento = findViewById(R.id.fabUploadDocumento)

        textSelectType.setOnClickListener { showTipoDocumentoDialog() }
        buttonSelectFile.setOnClickListener { selectFile() }
        fabUploadDocumento.setOnClickListener { uploadDocumento() }
    }

    private fun showTipoDocumentoDialog() {
        val types = TipoDocumento.values().map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Seleziona Tipo Documento")
            .setItems(types) { _, which ->
                selectedTipoDocumento = TipoDocumento.values()[which]
                textSelectType.text = "Tipo: ${selectedTipoDocumento.name}"
            }
            .show()
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickFileLauncher.launch(intent)
    }

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            selectedFileUri = it.data?.data
            textSelectedFileName.text = selectedFileUri?.lastPathSegment ?: "Nessun file selezionato"
        }
    }

    private fun uploadDocumento() {
        val titolo = editTitolo.text.toString().trim()
        val descrizione = editDescrizione.text.toString().trim()
        val visibile = switchVisible.isChecked
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (titolo.isBlank() || descrizione.isBlank() || selectedFileUri == null) {
            Snackbar.make(findViewById(android.R.id.content), "Compila tutti i campi e seleziona un file", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (currentUser == null) {
            Snackbar.make(findViewById(android.R.id.content), "Utente non autenticato", Snackbar.LENGTH_SHORT).show()
            return
        }

        fabUploadDocumento.isEnabled = false // Disabilita il pulsante durante l'upload

        lifecycleScope.launch {
            try {
                // 1. Upload del file su Firebase Storage
                val downloadUrl = storageManager.uploadFile(selectedFileUri!!, "documenti/${titolo}_${System.currentTimeMillis()}").await()

                // 2. Aggiunta dei metadati del documento a Firestore
                val documento = Documento(
                    titolo = titolo,
                    descrizione = descrizione,
                    url = downloadUrl.toString(),
                    dataCaricamento = Date(),
                    tipo = selectedTipoDocumento,
                    autore = currentUser.email ?: "Sconosciuto",
                    visibile = visibile
                )

                databaseManager.addDocumento(documento).fold(
                    onSuccess = {
                        Snackbar.make(findViewById(android.R.id.content), "Documento caricato con successo!", Snackbar.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = { exception ->
                        Snackbar.make(findViewById(android.R.id.content), "Errore nel salvataggio del documento: ${exception.message}", Snackbar.LENGTH_LONG).show()
                    }
                )
            } catch (e: Exception) {
                Snackbar.make(findViewById(android.R.id.content), "Errore nell'upload del file: ${e.message}", Snackbar.LENGTH_LONG).show()
            } finally {
                fabUploadDocumento.isEnabled = true
            }
        }
    }
} 