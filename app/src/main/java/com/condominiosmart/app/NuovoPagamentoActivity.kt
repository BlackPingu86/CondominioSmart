package com.condominiosmart.app

import android.app.Activity
import android.app.DatePickerDialog
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
import com.condominiosmart.app.models.Pagamento
import com.condominiosmart.app.models.TipoPagamento
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NuovoPagamentoActivity : AppCompatActivity() {

    private lateinit var databaseManager: FirebaseDatabaseManager
    private lateinit var storageManager: FirebaseStorageManager

    private lateinit var editTitolo: TextInputEditText
    private lateinit var editDescrizione: TextInputEditText
    private lateinit var editImporto: TextInputEditText
    private lateinit var textSelectType: TextView
    private lateinit var textDataScadenza: TextView
    private lateinit var textDataPagamento: TextView
    private lateinit var textSelectedFileName: TextView
    private lateinit var buttonSelectFile: android.widget.Button
    private lateinit var switchPagato: SwitchMaterial
    private lateinit var switchVisible: SwitchMaterial
    private lateinit var fabSalvaPagamento: FloatingActionButton

    private var selectedFileUri: Uri? = null
    private var selectedTipoPagamento: TipoPagamento = TipoPagamento.ALTRO
    private var dataScadenza: Date? = null
    private var dataPagamento: Date? = null

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuovo_pagamento)

        databaseManager = FirebaseDatabaseManager()
        storageManager = FirebaseStorageManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Inizializza le view
        editTitolo = findViewById(R.id.editTitolo)
        editDescrizione = findViewById(R.id.editDescrizione)
        editImporto = findViewById(R.id.editImporto)
        textSelectType = findViewById(R.id.textSelectType)
        textDataScadenza = findViewById(R.id.textDataScadenza)
        textDataPagamento = findViewById(R.id.textDataPagamento)
        textSelectedFileName = findViewById(R.id.textSelectedFileName)
        buttonSelectFile = findViewById(R.id.buttonSelectFile)
        switchPagato = findViewById(R.id.switchPagato)
        switchVisible = findViewById(R.id.switchVisible)
        fabSalvaPagamento = findViewById(R.id.fabSalvaPagamento)

        textSelectType.setOnClickListener { showTipoPagamentoDialog() }
        textDataScadenza.setOnClickListener { showDatePickerDialog(textDataScadenza) { date -> dataScadenza = date } }
        textDataPagamento.setOnClickListener { showDatePickerDialog(textDataPagamento) { date -> dataPagamento = date } }
        buttonSelectFile.setOnClickListener { selectFile() }
        fabSalvaPagamento.setOnClickListener { salvaPagamento() }
    }

    private fun showTipoPagamentoDialog() {
        val types = TipoPagamento.values().map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Seleziona Tipo Pagamento")
            .setItems(types) { _, which ->
                selectedTipoPagamento = TipoPagamento.values()[which]
                textSelectType.text = "Tipo: ${selectedTipoPagamento.name}"
            }
            .show()
    }

    private fun showDatePickerDialog(textView: TextView, onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSelected(calendar.time)
                textView.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "application/pdf"))
        }
        pickFileLauncher.launch(intent)
    }

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            selectedFileUri = it.data?.data
            val fileName = selectedFileUri?.lastPathSegment ?: "Nessun file selezionato"
            textSelectedFileName.text = fileName
            
            // Animazione di successo per la selezione file
            textSelectedFileName.animate()
                .alpha(0.5f)
                .setDuration(100)
                .withEndAction {
                    textSelectedFileName.animate()
                        .alpha(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
            
            // Mostra feedback positivo
            showFileSelectedSnackbar(fileName)
        } else {
            showErrorSnackbar("Nessun file selezionato")
        }
    }

    private fun showFileSelectedSnackbar(fileName: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), "File selezionato: $fileName", Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun salvaPagamento() {
        val titolo = editTitolo.text.toString().trim()
        val descrizione = editDescrizione.text.toString().trim()
        val importoText = editImporto.text.toString().trim()
        val pagato = switchPagato.isChecked
        val visibile = switchVisible.isChecked
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (titolo.isBlank() || descrizione.isBlank() || importoText.isBlank() || selectedFileUri == null) {
            showErrorSnackbar("Compila tutti i campi obbligatori e seleziona un file")
            return
        }

        if (currentUser == null) {
            showErrorSnackbar("Utente non autenticato")
            return
        }

        val importo = importoText.replace(',', '.').toDoubleOrNull()
        if (importo == null) {
            showErrorSnackbar("Importo non valido")
            return
        }

        // Disabilita il pulsante e mostra animazione
        fabSalvaPagamento.isEnabled = false
        fabSalvaPagamento.animate()
            .alpha(0.5f)
            .setDuration(200)
            .start()

        // Mostra Snackbar di caricamento
        val loadingSnackbar = showLoadingSnackbar("Salvataggio pagamento in corso...")

        lifecycleScope.launch {
            try {
                // 1. Upload del file su Firebase Storage
                val fileName = "pagamenti/${titolo}_${System.currentTimeMillis()}.${getFileExtension(selectedFileUri!!)}"
                val downloadUrl = storageManager.uploadFile(selectedFileUri!!, fileName).await()

                // 2. Aggiunta dei metadati del pagamento a Firestore
                val pagamento = Pagamento(
                    titolo = titolo,
                    descrizione = descrizione,
                    importo = importo,
                    dataScadenza = dataScadenza,
                    dataPagamento = dataPagamento,
                    urlFile = downloadUrl.toString(),
                    tipoPagamento = selectedTipoPagamento,
                    autore = currentUser.email ?: "Sconosciuto",
                    pagato = pagato,
                    visibile = visibile
                )

                databaseManager.addPagamento(pagamento).fold(
                    onSuccess = {
                        loadingSnackbar.dismiss()
                        showSuccessSnackbar()
                    },
                    onFailure = { exception ->
                        loadingSnackbar.dismiss()
                        showErrorSnackbar("Errore nel salvataggio del pagamento: ${exception.message}")
                        reEnableFab()
                    }
                )
            } catch (e: Exception) {
                loadingSnackbar.dismiss()
                showErrorSnackbar("Errore nell'upload del file: ${e.message}")
                reEnableFab()
            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = android.webkit.MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
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

    private fun showSuccessSnackbar() {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        val snackbarLayout = snackbarView as Snackbar.SnackbarLayout
        
        val customView = layoutInflater.inflate(R.layout.snackbar_save_success, null)
        snackbarLayout.addView(customView, 0)
        
        snackbar.show()
        
        // Chiudi l'activity dopo il successo
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                finish()
            }
        })
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun reEnableFab() {
        fabSalvaPagamento.isEnabled = true
        fabSalvaPagamento.animate()
            .alpha(1f)
            .setDuration(200)
            .start()
    }
} 