package com.example.cartei_mobile_app.ui.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cartei_mobile_app.R
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import com.example.cartei_mobile_app.data.AppDatenbank
import com.example.cartei_mobile_app.data.Karte
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class CreateCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)

        val frageFeld = findViewById<EditText>(R.id.input_frage)
        val antwortFeld = findViewById<EditText>(R.id.input_antwort)
        val speichernButton = findViewById<Button>(R.id.button_speichern)

        // Daten aus Intent lesen
        val karteId = intent.getIntExtra("karteId", -1)
        val alteFrage = intent.getStringExtra("frage") ?: ""
        val alteAntwort = intent.getStringExtra("antwort") ?: ""

        frageFeld.setText(alteFrage)
        antwortFeld.setText(alteAntwort)

        speichernButton.setOnClickListener {
            val frage = frageFeld.text.toString().trim()
            val antwort = antwortFeld.text.toString().trim()

            if (frage.isEmpty() || antwort.isEmpty()) {
                Toast.makeText(this, "Bitte beide Felder ausfüllen", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatenbank.getDatenbank(applicationContext)
                    val karte = Karte(id = if (karteId != -1) karteId else 0, frage = frage, antwort = antwort, satzId = 0)

                    if (karteId != -1) {
                        db.kartenDao().karteAktualisieren(karte)
                    } else {
                        db.kartenDao().karteEinfügen(karte)
                    }

                    runOnUiThread {
                        Toast.makeText(this@CreateCardActivity, "Karte gespeichert", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }


        }

    }

}








