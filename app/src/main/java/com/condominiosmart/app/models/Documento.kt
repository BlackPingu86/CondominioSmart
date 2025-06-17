package com.condominiosmart.app.models

import java.io.Serializable
import java.util.Date

data class Documento(
    val id: String = "",
    val titolo: String = "",
    val descrizione: String = "",
    val url: String = "",
    val dataCaricamento: Date = Date(),
    val tipo: TipoDocumento = TipoDocumento.ALTRO,
    val autore: String = "",
    val visibile: Boolean = true
) : Serializable

enum class TipoDocumento {
    AMMINISTRATORE,
    ASSEMBLEA,
    CONTABILITA,
    MANUTENZIONE,
    ALTRO
} 