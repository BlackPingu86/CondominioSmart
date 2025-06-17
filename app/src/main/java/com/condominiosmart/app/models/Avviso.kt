package com.condominiosmart.app.models

import java.io.Serializable
import java.util.Date

data class Avviso(
    val id: String = "",
    val titolo: String = "",
    val contenuto: String = "",
    val data: Date = Date(),
    val dataScadenza: Date? = null,
    val autore: String = "",
    val importante: Boolean = false
) : Serializable 