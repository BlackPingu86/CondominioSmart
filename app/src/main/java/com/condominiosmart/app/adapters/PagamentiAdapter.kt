package com.condominiosmart.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.condominiosmart.app.DettaglioPagamentoActivity
import com.condominiosmart.app.R
import com.condominiosmart.app.models.Pagamento
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PagamentiAdapter(
    private var pagamenti: List<Pagamento>,
    private val context: Context
) : RecyclerView.Adapter<PagamentiAdapter.PagamentoViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY)

    inner class PagamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitolo: TextView = itemView.findViewById(R.id.textTitolo)
        val textImporto: TextView = itemView.findViewById(R.id.textImporto)
        val textTipo: TextView = itemView.findViewById(R.id.textTipo)
        val textDataScadenza: TextView = itemView.findViewById(R.id.textDataScadenza)
        val textStatoPagamento: TextView = itemView.findViewById(R.id.textStatoPagamento)
        val iconStatoPagamento: ImageView = itemView.findViewById(R.id.iconStatoPagamento)
        val imageAnteprima: ImageView = itemView.findViewById(R.id.imageAnteprima)
        val iconFileType: ImageView = itemView.findViewById(R.id.iconFileType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagamentoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pagamento, parent, false)
        return PagamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagamentoViewHolder, position: Int) {
        val pagamento = pagamenti[position]
        holder.textTitolo.text = pagamento.titolo
        holder.textImporto.text = currencyFormat.format(pagamento.importo)
        holder.textTipo.text = pagamento.tipoPagamento.name

        pagamento.dataScadenza?.let { 
            holder.textDataScadenza.text = "Scadenza: ${dateFormat.format(it)}"
            holder.textDataScadenza.visibility = View.VISIBLE
        } ?: run { 
            holder.textDataScadenza.visibility = View.GONE
        }

        if (pagamento.pagato) {
            holder.textStatoPagamento.text = "Pagato"
            holder.textStatoPagamento.setTextColor(ContextCompat.getColor(context, R.color.green_paid))
            holder.iconStatoPagamento.setImageResource(R.drawable.ic_check_circle)
            holder.iconStatoPagamento.setColorFilter(ContextCompat.getColor(context, R.color.green_paid))
        } else {
            holder.textStatoPagamento.text = "Da pagare"
            holder.textStatoPagamento.setTextColor(ContextCompat.getColor(context, R.color.red_unpaid))
            holder.iconStatoPagamento.setImageResource(R.drawable.ic_warning)
            holder.iconStatoPagamento.setColorFilter(ContextCompat.getColor(context, R.color.red_unpaid))
        }

        // Gestione anteprima file
        setupFilePreview(holder, pagamento.urlFile)

        // Gestione del click sull'item
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DettaglioPagamentoActivity::class.java).apply {
                putExtra("pagamento", pagamento)
            }
            context.startActivity(intent)
        }
    }

    private fun setupFilePreview(holder: PagamentoViewHolder, urlFile: String) {
        if (urlFile.isNotEmpty()) {
            val fileExtension = getFileExtension(urlFile).lowercase()
            
            when {
                fileExtension in listOf("jpg", "jpeg", "png", "gif", "bmp") -> {
                    // È un'immagine, mostra l'anteprima
                    holder.iconFileType.visibility = View.GONE
                    holder.imageAnteprima.visibility = View.VISIBLE
                    
                    Glide.with(context)
                        .load(urlFile)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .centerCrop()
                        .into(holder.imageAnteprima)
                }
                fileExtension == "pdf" -> {
                    // È un PDF, mostra l'icona PDF
                    holder.imageAnteprima.visibility = View.GONE
                    holder.iconFileType.visibility = View.VISIBLE
                    holder.iconFileType.setImageResource(R.drawable.ic_pdf)
                    holder.iconFileType.setColorFilter(ContextCompat.getColor(context, R.color.red_unpaid))
                }
                else -> {
                    // File non riconosciuto, mostra icona generica
                    holder.imageAnteprima.visibility = View.GONE
                    holder.iconFileType.visibility = View.VISIBLE
                    holder.iconFileType.setImageResource(R.drawable.ic_upload)
                    holder.iconFileType.setColorFilter(ContextCompat.getColor(context, R.color.colorGray))
                }
            }
        } else {
            // Nessun file allegato
            holder.imageAnteprima.visibility = View.GONE
            holder.iconFileType.visibility = View.GONE
        }
    }

    private fun getFileExtension(url: String): String {
        return try {
            url.substringAfterLast('.', "")
        } catch (e: Exception) {
            ""
        }
    }

    override fun getItemCount() = pagamenti.size

    fun updatePagamenti(newPagamenti: List<Pagamento>) {
        pagamenti = newPagamenti
        notifyDataSetChanged()
    }
} 