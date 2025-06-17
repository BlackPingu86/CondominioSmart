package com.condominiosmart.app.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.condominiosmart.app.DettaglioDocumentoActivity
import com.condominiosmart.app.R
import com.condominiosmart.app.models.Documento
import java.text.SimpleDateFormat
import java.util.Locale

class DocumentiAdapter(
    private var documenti: List<Documento>,
    private val context: Context
) : RecyclerView.Adapter<DocumentiAdapter.DocumentoViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class DocumentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitolo: TextView = itemView.findViewById(R.id.textTitolo)
        val textTipo: TextView = itemView.findViewById(R.id.textTipo)
        val textData: TextView = itemView.findViewById(R.id.textData)
        val textAutore: TextView = itemView.findViewById(R.id.textAutore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_documento, parent, false)
        return DocumentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentoViewHolder, position: Int) {
        val documento = documenti[position]
        holder.textTitolo.text = documento.titolo
        holder.textTipo.text = documento.tipo.name
        holder.textData.text = dateFormat.format(documento.dataCaricamento)
        holder.textAutore.text = "Caricato da: ${documento.autore}"

        // Gestione del click sull'item
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DettaglioDocumentoActivity::class.java).apply {
                putExtra("documento", documento)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = documenti.size

    fun updateDocumenti(newDocumenti: List<Documento>) {
        documenti = newDocumenti
        notifyDataSetChanged()
    }
} 