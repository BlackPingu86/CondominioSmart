package com.condominiosmart.app.models

import java.io.Serializable
import java.util.Date

data class Pagamento(
    val id: String = "",
    val titolo: String = "",
    val descrizione: String = "",
    val importo: Double = 0.0,
    val dataScadenza: Date? = null,
    val dataPagamento: Date? = null,
    val urlFile: String = "",
    val tipoPagamento: TipoPagamento = TipoPagamento.ALTRO,
    val autore: String = "",
    val pagato: Boolean = false,
    val visibile: Boolean = true
) : Serializable

enum class TipoPagamento {
    FATTURA,
    BOLLETTA,
    RICEVUTA,
    ALTRO
} 