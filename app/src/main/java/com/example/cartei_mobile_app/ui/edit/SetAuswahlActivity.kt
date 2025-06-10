package com.example.cartei_mobile_app.ui.edit

import android.os.Bundle
import com.example.cartei_mobile_app.data.AppDatenbank
import com.example.cartei_mobile_app.ui.viewer.KartensetAdapter
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartei_mobile_app.R


class SetAuswahlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_auswahl)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Kartenset auswählen"


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSets)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = KartensetAdapter(emptyList()) { satz ->
            val intent = Intent(this, SetBearbeitenActivity::class.java)
            intent.putExtra("satzId", satz.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val db = AppDatenbank.getDatenbank(this)
        db.kartensatzDao().getAlleKartensätze().observe(this) { sets ->
            adapter.updateListe(sets)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Schließt die Activity und kehrt zur MainActivity zurück
        return true
    }


}
