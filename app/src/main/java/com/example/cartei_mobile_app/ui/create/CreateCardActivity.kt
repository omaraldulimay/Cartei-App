package com.example.cartei_mobile_app.ui.create

import android.os.Bundle
import android.util.Log
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
import com.example.cartei_mobile_app.data.Kartensatz


private var aktuellerSatzId: Int = -1

class CreateCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)

        val titelFeld = findViewById<EditText>(R.id.input_titel)
        val frageFeld = findViewById<EditText>(R.id.input_frage)
        val antwortFeld = findViewById<EditText>(R.id.input_antwort)
        val speichernButton = findViewById<Button>(R.id.button_speichern)

        val alteFrage = intent.getStringExtra("frage") ?: ""
        val alteAntwort = intent.getStringExtra("antwort") ?: ""
        val alterTitel = intent.getStringExtra("titel") ?: ""

        frageFeld.setText(alteFrage)
        antwortFeld.setText(alteAntwort)
        titelFeld.setText(alterTitel)


        speichernButton.setOnClickListener {
            val frage = frageFeld.text.toString().trim()
            val antwort = antwortFeld.text.toString().trim()
            val titel = titelFeld.text.toString().trim()

            if (frage.isEmpty() || antwort.isEmpty() || titel.isEmpty()) {
                Toast.makeText(this, "Alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatenbank.getDatenbank(applicationContext)
                val kartensatzDao = db.kartensatzDao()
                val kartenDao = db.kartenDao()

                // Satz-ID prüfen oder merken
                if (aktuellerSatzId == -1) {
                    val bestehenderSatz = kartensatzDao.getSatzMitTitel(titel)
                    aktuellerSatzId = bestehenderSatz?.id
                        ?: kartensatzDao.insert(Kartensatz(titel = titel)).toInt()
                    runOnUiThread {
                        Toast.makeText(this@CreateCardActivity, "Satz-ID: $aktuellerSatzId", Toast.LENGTH_SHORT).show()
                    }

                }

                val neueKarte = Karte(
                    frage = frage,
                    antwort = antwort,
                    satzId = aktuellerSatzId
                )

                kartenDao.karteEinfügen(neueKarte)

                runOnUiThread {
                    frageFeld.setText("")
                    antwortFeld.setText("")
                    Toast.makeText(this@CreateCardActivity, "Karte gespeichert", Toast.LENGTH_SHORT)
                        .show()


                }
            }
        }
    }
}






