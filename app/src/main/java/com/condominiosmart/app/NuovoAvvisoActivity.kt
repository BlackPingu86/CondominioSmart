package com.condominiosmart.app

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.condominiosmart.app.firebase.FirebaseDatabaseManager
import com.condominiosmart.app.models.Avviso
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NuovoAvvisoActivity : AppCompatActivity() {
    private lateinit var databaseManager: FirebaseDatabaseManager
    private var dataScadenza: Date? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuovo_avviso)

        databaseManager = FirebaseDatabaseManager()

        // Setup della toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val editTitolo = findViewById<TextInputEditText>(R.id.editTitolo)
        val editContenuto = findViewById<TextInputEditText>(R.id.editContenuto)
        val switchImportante = findViewById<SwitchMaterial>(R.id.switchImportante)
        val textDataScadenza = findViewById<TextView>(R.id.textDataScadenza)

        // Setup del selettore di data
        textDataScadenza.setOnClickListener {
            showDatePickerDialog { date ->
                dataScadenza = date
                textDataScadenza.text = dateFormat.format(date)
            }
        }

        // Setup del FAB
        findViewById<FloatingActionButton>(R.id.fabSalva).setOnClickListener {
            val titolo = editTitolo.text.toString()
            val contenuto = editContenuto.text.toString()
            val importante = switchImportante.isChecked
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (titolo.isBlank() || contenuto.isBlank()) {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val avviso = Avviso(
                titolo = titolo,
                contenuto = contenuto,
                data = Date(),
                dataScadenza = dataScadenza,
                autore = currentUser?.email ?: "Utente",
                importante = importante
            )

            lifecycleScope.launch {
                try {
                    databaseManager.addAvviso(avviso).fold(
                        onSuccess = {
                            Toast.makeText(this@NuovoAvvisoActivity, "Avviso creato con successo", Toast.LENGTH_SHORT).show()
                            finish()
                        },
                        onFailure = { exception ->
                            Toast.makeText(this@NuovoAvvisoActivity, "Errore: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                } catch (e: Exception) {
                    Toast.makeText(this@NuovoAvvisoActivity, "Errore: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSelected(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
} 