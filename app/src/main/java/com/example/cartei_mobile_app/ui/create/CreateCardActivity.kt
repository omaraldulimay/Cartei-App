package com.example.cartei_mobile_app.ui.create

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cartei_mobile_app.R
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.cartei_mobile_app.data.AppDatenbank
import com.example.cartei_mobile_app.data.Karte
import com.example.cartei_mobile_app.data.Kartensatz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private var aktuellerSatzId: Int = -1

class CreateCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)

        // AppBar-Zurückpfeil aktivieren & Titel setzen
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Kartenset erstellen"

        val frageText = findViewById<EditText>(R.id.edit_frage)
        val antwortText = findViewById<EditText>(R.id.edit_antwort)
        val buttonSpeichern = findViewById<Button>(R.id.button_speichern)
        val buttonFertig = findViewById<Button>(R.id.button_fertig)
        val titelEdit = findViewById<EditText>(R.id.edit_set_titel)
        val frageVorher = intent.getStringExtra("frage") ?: ""
        val antwortVorher = intent.getStringExtra("antwort") ?: ""
        frageText.setText(frageVorher)
        antwortText.setText(antwortVorher)


        val übergebeneSatzId = intent.getIntExtra("satzId", -1)
        val karteId = intent.getIntExtra("karteId", -1)

        val titel = titelEdit.text.toString()


        if (übergebeneSatzId == -1) {
            // Neuen Kartensatz erstellen, wenn keiner übergeben wurde
            lifecycleScope.launch {
                val neuerSatz = Kartensatz(titel = titel)
                val neueSatzId = AppDatenbank.getDatenbank(this@CreateCardActivity).kartensatzDao()
                    .insert(neuerSatz).toInt()
                aktuellerSatzId = neueSatzId
                Log.d("CreateCard", "Neuer Kartensatz erstellt mit ID: $neueSatzId")
            }
        } else {
            aktuellerSatzId = übergebeneSatzId
        }

        buttonSpeichern.setOnClickListener {
            val frage = frageText.text.toString()
            val antwort = antwortText.text.toString()

            if (frage.isBlank() || antwort.isBlank()) {
                Toast.makeText(this, "Frage und Antwort dürfen nicht leer sein", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (aktuellerSatzId == -1) {
                Toast.makeText(this, "Kartensatz-ID nicht verfügbar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val karte = Karte(
                id = if (karteId != -1) karteId else 0, // Wenn vorhanden, bestehende Karte überschreiben
                frage = frage,
                antwort = antwort,
                satzId = aktuellerSatzId,
                gelernt = false
            )

            lifecycleScope.launch {
                val dao = AppDatenbank.getDatenbank(this@CreateCardActivity).kartenDao()
                if (karteId != -1) {
                    dao.update(karte) // Update der Karte
                } else {
                    dao.karteEinfügen(karte) // Neue Karte
                }

                withContext(Dispatchers.Main) {
                    frageText.text.clear()
                    antwortText.text.clear()
                    frageText.requestFocus()
                    Toast.makeText(this@CreateCardActivity, "Karte gespeichert", Toast.LENGTH_SHORT)
                        .show()
                }
            }


            buttonFertig.setOnClickListener {
                val eingegebenerTitel = titelEdit.text.toString()

                if (aktuellerSatzId == -1) {
                    if (eingegebenerTitel.isBlank()) {
                        Toast.makeText(this, "Bitte gib dem Set einen Titel", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }

                    // Neuen Satz speichern
                    lifecycleScope.launch {
                        val neuerSatz = Kartensatz(titel = eingegebenerTitel)
                        aktuellerSatzId = AppDatenbank.getDatenbank(this@CreateCardActivity)
                            .kartensatzDao()
                            .insert(neuerSatz)
                            .toInt()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@CreateCardActivity,
                                "Kartensatz gespeichert",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }

                } else {
                    // Optional: Titel aktualisieren
                    lifecycleScope.launch {
                        val aktualisierterSatz =
                            Kartensatz(id = aktuellerSatzId, titel = eingegebenerTitel)
                        AppDatenbank.getDatenbank(this@CreateCardActivity).kartensatzDao()
                            .update(aktualisierterSatz)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@CreateCardActivity,
                                "Kartensatz aktualisiert",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


}
