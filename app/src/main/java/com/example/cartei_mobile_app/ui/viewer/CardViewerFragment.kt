package com.example.cartei_mobile_app.ui.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cartei_mobile_app.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartei_mobile_app.viewmodel.KartenViewModel
import com.example.cartei_mobile_app.viewmodel.KartenViewModelFactory
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import com.example.cartei_mobile_app.data.Karte
import android.content.Intent
import com.example.cartei_mobile_app.ui.create.CreateCardActivity
import android.widget.ToggleButton




private lateinit var textFortschritt: TextView


class CardViewerFragment : Fragment() {

    private lateinit var viewModel: KartenViewModel
    private lateinit var textKarte: TextView
    private lateinit var buttonNaechste: Button

    private var aktuelleIndex = 0
    private var zeigeAntwort = false
    private var kartenListe: List<Karte> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_card_viewer, container, false)

        textKarte = view.findViewById(R.id.text_karte)
        buttonNaechste = view.findViewById(R.id.button_naechste)

        val factory = KartenViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[KartenViewModel::class.java]

        viewModel.alleKarten.observe(viewLifecycleOwner) { liste ->
            alleKartenOriginal = liste
            filtereKartenUndZeige()
        }


        // Normaler Klick: flippen
        textKarte.setOnClickListener {
            zeigeAntwort = !zeigeAntwort
            zeigeKarte()


            buttonGelernt = view.findViewById(R.id.button_gelernt)
            buttonGelernt.setOnClickListener {
                val karte = kartenListe[aktuelleIndex]
                val neueKarte = karte.copy(gelernt = true)
                viewModel.karteAktualisieren(neueKarte)
                Toast.makeText(requireContext(), "Als gelernt markiert", Toast.LENGTH_SHORT).show()
            }

            toggleNurUngelernt = view.findViewById(R.id.toggle_nur_ungelernt)
            toggleNurUngelernt.setOnCheckedChangeListener { _, isChecked ->
                nurUngelerntAnzeigen = isChecked
                filtereKartenUndZeige()
            }


        }

// Langer Klick: bearbeiten
        textKarte.setOnLongClickListener {
            val karte = kartenListe[aktuelleIndex]
            val intent = Intent(requireContext(), CreateCardActivity::class.java).apply {
                putExtra("karteId", karte.id)
                putExtra("frage", karte.frage)
                putExtra("antwort", karte.antwort)
            }
            startActivity(intent)
            true // wichtig: gibt zurück, dass der LongClick behandelt wurde
        }


        buttonNaechste.setOnClickListener {
            if (aktuelleIndex < kartenListe.size - 1) {
                aktuelleIndex++
                zeigeAntwort = false
                zeigeKarte()
            } else {
                Toast.makeText(requireContext(), "Letzte Karte erreicht", Toast.LENGTH_SHORT).show()
            }
        }

        textFortschritt = view.findViewById(R.id.text_fortschritt)


        return view
    }

    private fun zeigeKarte() {
        if (kartenListe.isNotEmpty()) {
            val aktuelle = kartenListe[aktuelleIndex]
            textKarte.text = if (zeigeAntwort) aktuelle.antwort else aktuelle.frage
        } else {
            textKarte.text = "Keine Karten vorhanden"
        }

        val gelerntCount = kartenListe.count { it.gelernt }
        textFortschritt.text = "Karte ${aktuelleIndex + 1} von ${kartenListe.size}  |  $gelerntCount gelernt"

    }

    private lateinit var buttonGelernt: Button

    private lateinit var toggleNurUngelernt: ToggleButton
    private var nurUngelerntAnzeigen = false
    private var alleKartenOriginal: List<Karte> = emptyList()

    private fun filtereKartenUndZeige() {
        kartenListe = if (nurUngelerntAnzeigen) {
            alleKartenOriginal.filter { !it.gelernt }
        } else {
            alleKartenOriginal
        }

        aktuelleIndex = 0
        zeigeAntwort = false
        zeigeKarte()
    }






}

