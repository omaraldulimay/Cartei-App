package com.example.cartei_mobile_app.ui.main

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.data.AppDatenbank
import com.example.cartei_mobile_app.data.Karte
import kotlinx.coroutines.launch


class LernActivity : AppCompatActivity() {

    private lateinit var karteText: TextView
    private lateinit var karten: List<Karte>
    private var aktuelleIndex = 0

    private var istFrage = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lernen"

        setContentView(R.layout.activity_lern)

        karteText = findViewById(R.id.karteText)

        val fortschrittText = findViewById<TextView>(R.id.fortschrittText)
        val buttonNaechste = findViewById<Button>(R.id.button_naechste)
        val buttonGelernt = findViewById<Button>(R.id.button_gelernt)
        val satzId = intent.getIntExtra("satzId", -1)

        if (satzId == -1) {
            Toast.makeText(this, "Fehler: Kein Kartensatz gefunden", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = AppDatenbank.getDatenbank(this)
        db.kartenDao().alleKartenFürSatz(satzId).observe(this) { kartenListe ->
            if (kartenListe.isNullOrEmpty()) {
                karteText.text = "Keine Karten im Set"
            } else {
                karten = kartenListe
                zeigeKarte(0)
            }
        }

        karteText.setOnClickListener {
            if (!::karten.isInitialized || karten.isEmpty()) return@setOnClickListener

            val aktuelleKarte = karten[aktuelleIndex]
            val newText = if (istFrage) aktuelleKarte.antwort else aktuelleKarte.frage
            istFrage = !istFrage

            karteText.animate().rotationYBy(180f).setDuration(300).withEndAction {
                karteText.text = newText
                karteText.rotationY = 0f
            }.start()
        }


        buttonNaechste.setOnClickListener {
            if (::karten.isInitialized && aktuelleIndex < karten.size - 1) {
                zeigeKarte(aktuelleIndex + 1)
            } else {
                Toast.makeText(this, "Letzte Karte erreicht", Toast.LENGTH_SHORT).show()
            }
        }

        buttonGelernt.setOnClickListener {
            if (::karten.isInitialized && aktuelleIndex in karten.indices) {
                val aktuelleKarte = karten[aktuelleIndex]
                aktuelleKarte.gelernt = true
                lifecycleScope.launch {
                    AppDatenbank.getDatenbank(this@LernActivity).kartenDao().karteAktualisieren(aktuelleKarte)
                }

                Toast.makeText(this, "Als gelernt markiert", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun zeigeKarte(index: Int) {
        if (::karten.isInitialized && index in karten.indices) {
            val karte = karten[index]
            karteText.text = karte.frage
            istFrage = true
            aktuelleIndex = index

            val gelernt = karten.count { it.gelernt }
            val gesamt = karten.size
            val fortschrittText = findViewById<TextView>(R.id.fortschrittText)
            fortschrittText.text = "Karte ${index + 1} von $gesamt – $gelernt gelernt"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // ← schließt die LernActivity und kehrt zurück zur MainActivity
        return true
    }


}
