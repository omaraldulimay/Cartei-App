package com.example.cartei_mobile_app.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cartei_mobile_app.R
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.cartei_mobile_app.viewmodel.KartenViewModel
import com.example.cartei_mobile_app.viewmodel.KartenViewModelFactory
import android.widget.TextView




class ProgressFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)

        val loeschenButton = view.findViewById<Button>(R.id.button_karten_loeschen)

        val factory = KartenViewModelFactory(requireActivity().application)
        val viewModel = ViewModelProvider(this, factory)[KartenViewModel::class.java]

        loeschenButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Karten löschen")
                .setMessage("Möchtest du wirklich alle Karten löschen?")
                .setPositiveButton("Ja") { _, _ ->
                    viewModel.alleKartenLöschen()
                    Toast.makeText(requireContext(), "Alle Karten gelöscht", Toast.LENGTH_SHORT)
                        .show()
                }
                .setNegativeButton("Abbrechen", null)
                .show()

            val textProgress = view.findViewById<TextView>(R.id.text_progress)

            viewModel.alleKarten.observe(viewLifecycleOwner) { karten ->
                val gesamt = karten.size
                val gelernt = karten.count { it.gelernt }
                textProgress.text = "Gelernt: $gelernt von $gesamt Karten"
            }

        }

        return view
    }
}

