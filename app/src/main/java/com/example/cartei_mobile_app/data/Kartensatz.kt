package com.example.cartei.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kartensätze")
data class Kartensatz(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titel: String,
    val farbe: String? = null  // optional: Farbcode zur Unterscheidung
)
