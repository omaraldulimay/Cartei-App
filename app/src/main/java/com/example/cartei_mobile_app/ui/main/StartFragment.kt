package com.example.cartei_mobile_app.ui.main

import com.example.cartei_mobile_app.ui.main.LernActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.data.AppDatenbank
import com.example.cartei_mobile_app.ui.create.CreateCardActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        val neuerSatzBtn = view.findViewById<Button>(R.id.button_neuer_satz)
        val lernenBtn = view.findViewById<Button>(R.id.button_lernen_starten)

        neuerSatzBtn.setOnClickListener {
            val intent = Intent(requireContext(), CreateCardActivity::class.java)
            startActivity(intent)
        }

        lernenBtn.setOnClickListener {
            val db = AppDatenbank.getDatenbank(requireContext())
            db.kartensatzDao().getAlleKartensätze().observe(viewLifecycleOwner) { sätze ->
                if (sätze.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Keine Kartensätze vorhanden",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@observe
                }

                val dialogView = layoutInflater.inflate(R.layout.dialog_kartensatz_liste, null)
                val container = dialogView.findViewById<LinearLayout>(R.id.container_kartensätze)

                sätze.forEach { satz ->
                    val zeile = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.HORIZONTAL
                        val padding = (8 * resources.displayMetrics.density).toInt()
                        setPadding(padding, padding, padding, padding)
                    }

                    val titelText = TextView(requireContext()).apply {
                        text = satz.titel
                        layoutParams =
                            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                        textSize = 16f
                    }

                    val löschenButton = Button(requireContext()).apply {
                        text = "❌"
                        setOnClickListener {
                            val dialog = AlertDialog.Builder(requireContext())
                                .setTitle("Löschen?")
                                .setMessage("Kartensatz \"${satz.titel}\" wirklich löschen?")
                                .setPositiveButton("Ja", null)
                                .setNegativeButton("Abbrechen", null)
                                .create()

                            dialog.setOnShowListener {
                                val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                positive.setOnClickListener {
                                    dialog.dismiss()
                                    CoroutineScope(Dispatchers.IO).launch {
                                        db.kartensatzDao().deleteSatz(satz)
                                        db.kartenDao().alleKartenEinesSatzesLöschen(satz.id)
                                    }
                                }
                            }

                            dialog.show()
                        }
                    }

                    zeile.addView(titelText)
                    zeile.addView(löschenButton)
                    container.addView(zeile)

                    titelText.setOnClickListener {
                        Toast.makeText(
                            requireContext(),
                            "Klick auf: ${satz.titel} (ID: ${satz.id})",
                            Toast.LENGTH_SHORT
                        ).show()

                        speichereSatzId(satz.id)

                        if (satz.id > 0) {
                            Log.d("DEBUG", "Klick auf Kartensatz: ${satz.titel}, ID: ${satz.id}")

                            val bundle = Bundle().apply {
                                putInt("satzId", satz.id)

                                putString("modus", "lernen") // NEU: Lernmodus explizit setzen
                            }
                            parentFragmentManager.setFragmentResult("satzIdKey", bundle)
                            val intent = Intent(requireContext(), LernActivity::class.java)
                            intent.putExtra("satzId", satz.id)
                            startActivity(intent)


                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Fehler: Ungültiger Kartensatz",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                // ✅ Jetzt an richtiger Stelle: Dialog anzeigen
                AlertDialog.Builder(requireContext())
                    .setTitle("Kartensatz auswählen")
                    .setView(dialogView)
                    .setNegativeButton("Schließen", null)
                    .show()
            }


        }

        val letzterSatzId = holeGespeichertenSatzId()
        if (letzterSatzId > 0) {
            val textLetzterSatz = view.findViewById<TextView>(R.id.text_letzter_satz)
            val db = AppDatenbank.getDatenbank(requireContext())

            CoroutineScope(Dispatchers.IO).launch {
                val satz = db.kartensatzDao().getSatzMitId(letzterSatzId)
                if (satz != null) {
                    withContext(Dispatchers.Main) {
                        db.kartenDao().alleKartenFürSatz(letzterSatzId)
                            .observe(viewLifecycleOwner) { karten ->
                                val gelernt = karten.count { it.gelernt }
                                val gesamt = karten.size
                                textLetzterSatz.text =
                                    "Letztes Set: ${satz.titel} – Fortschritt: $gelernt von $gesamt"
                            }
                    }
                }
            }
        }


        return view
    }

     private fun speichereSatzId(satzId: Int) {
        val prefs = requireContext().getSharedPreferences("cartei_prefs", 0)
        prefs.edit().putInt("letzter_satz_id", satzId).apply()
    }

     private fun holeGespeichertenSatzId(): Int {
        val prefs = requireContext().getSharedPreferences("cartei_prefs", 0)
        return prefs.getInt("letzter_satz_id", -1)
    }




}




