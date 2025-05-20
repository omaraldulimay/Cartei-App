package com.example.cartei_mobile_app.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.data.Karte
import com.example.cartei_mobile_app.viewmodel.KartenViewModel
import com.example.cartei_mobile_app.viewmodel.KartenViewModelFactory

class ProgressFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)

        parentFragmentManager.setFragmentResultListener("satzIdKey", viewLifecycleOwner) { _, bundle ->
            val satzId = bundle.getInt("satzId", 0)

            val factory = KartenViewModelFactory(requireActivity().application, satzId)
            val viewModel = ViewModelProvider(this, factory)[KartenViewModel::class.java]

            val textProgress = view.findViewById<TextView>(R.id.text_progress)
            val loeschenButton = view.findViewById<Button>(R.id.button_karten_loeschen)
            val resetButton = view.findViewById<Button>(R.id.button_reset_gelernt)

            viewModel.alleKarten.observe(viewLifecycleOwner) { karten ->
                updateProgress(karten)
            }


            loeschenButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Karten löschen")
                    .setMessage("Möchtest du wirklich alle Karten löschen?")
                    .setPositiveButton("Ja") { _, _ ->
                        viewModel.alleKartenLöschen()
                        Toast.makeText(requireContext(), "Alle Karten gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Abbrechen", null)
                    .show()
            }

            resetButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Gelernt-Status zurücksetzen")
                    .setMessage("Willst du wirklich alle Karten auf 'ungelernt' setzen?")
                    .setPositiveButton("Ja") { _, _ ->
                        viewModel.gelerntZurücksetzen()

                        // Manuelles Update
                        view?.postDelayed({
                            viewModel.alleKarten.value?.let { updateProgress(it) }
                        }, 300)

                        Toast.makeText(requireContext(), "Gelernt-Status zurückgesetzt", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Abbrechen", null)
                    .show()
            }


        }

        return view
    }


    private fun updateProgress(karten: List<Karte>) {
        val gesamt = karten.size
        val gelernt = karten.count { it.gelernt }
        view?.findViewById<TextView>(R.id.text_progress)?.text =
            "Gelernt: $gelernt von $gesamt Karten"
    }



}

