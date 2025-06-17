package com.condominiosmart.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.condominiosmart.app.DettaglioAvvisoActivity
import com.condominiosmart.app.R
import com.condominiosmart.app.models.Avviso
import java.text.SimpleDateFormat
import java.util.Locale

class AvvisiAdapter(
    private var avvisi: List<Avviso>,
    private val context: Context
) : RecyclerView.Adapter<AvvisiAdapter.AvvisoViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    inner class AvvisoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitolo: TextView = itemView.findViewById(R.id.textTitolo)
        val textData: TextView = itemView.findViewById(R.id.textData)
        val textContenuto: TextView = itemView.findViewById(R.id.textContenuto)
        val textAutore: TextView = itemView.findViewById(R.id.textAutore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvvisoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_avviso, parent, false)
        return AvvisoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvvisoViewHolder, position: Int) {
        val avviso = avvisi[position]
        holder.textTitolo.text = avviso.titolo
        holder.textData.text = dateFormat.format(avviso.data)
        holder.textContenuto.text = avviso.contenuto
        holder.textAutore.text = "Pubblicato da: ${avviso.autore}"

        // Gestione del click sull'item
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DettaglioAvvisoActivity::class.java).apply {
                putExtra("avviso", avviso)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = avvisi.size

    fun updateAvvisi(newAvvisi: List<Avviso>) {
        avvisi = newAvvisi
        notifyDataSetChanged()
    }
} 