package com.example.cartei_mobile_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "karten")
data class Karte(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val frage: String,
    val antwort: String,
    val satzId: Int,  // Fremdschlüssel auf Kartensatz
    var gelernt: Boolean

)
