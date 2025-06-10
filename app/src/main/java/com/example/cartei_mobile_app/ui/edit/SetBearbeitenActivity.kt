package com.example.cartei_mobile_app.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.viewmodel.KartenViewModel
import com.example.cartei_mobile_app.viewmodel.KartenViewModelFactory
import android.widget.TextView
import com.example.cartei_mobile_app.ui.progress.KartenAdapter
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.cartei_mobile_app.ui.create.CreateCardActivity


class SetBearbeitenActivity : AppCompatActivity() {

    private lateinit var viewModel: KartenViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var kartenAdapter: KartenAdapter
    private var satzId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_bearbeiten)

        // AppBar-Zurückpfeil aktivieren & Titel setzen
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Kartenset bearbeiten"

        satzId = intent.getIntExtra("satzId", -1)
        if (satzId == -1) {
            finish()
            return
        }

        val titleText = findViewById<TextView>(R.id.text_satz_titel)
        titleText.text = "Kartenset #$satzId"

        recyclerView = findViewById(R.id.recyclerViewKartenBearbeiten)
        recyclerView.layoutManager = LinearLayoutManager(this)
        kartenAdapter = KartenAdapter(
            emptyList(),
            onClick = { karte ->
                val intent = Intent(this, CreateCardActivity::class.java)
                intent.putExtra("karteId", karte.id)
                intent.putExtra("frage", karte.frage)
                intent.putExtra("antwort", karte.antwort)
                intent.putExtra("titel", "Dein Titel hier") // Optional – Titel falls du ihn hast
                intent.putExtra("satzId", karte.satzId)
                startActivity(intent)
            }
        )

        recyclerView.adapter = kartenAdapter


        val factory = KartenViewModelFactory(application, satzId)
        viewModel = ViewModelProvider(this, factory)[KartenViewModel::class.java]

        viewModel.alleKarten.observe(this) { karten ->
            kartenAdapter.updateListe(karten)
        }

        val buttonZurueck = findViewById<Button>(R.id.button_zurueck)
        buttonZurueck.setOnClickListener {
            finish() // schließt die Activity und kehrt zurück zum Fragment
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}



