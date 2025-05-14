package com.example.cartei_mobile_app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cartei_mobile_app.R
import android.content.Intent
import com.example.cartei_mobile_app.ui.create.CreateCardActivity


class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        // Button: Lernen starten
        view.findViewById<Button>(R.id.button_lernen_starten).setOnClickListener {
            findNavController().navigate(R.id.nav_karten)
        }

        // Button: Neues Kartenset erstellen
        view.findViewById<Button>(R.id.button_neuer_satz).setOnClickListener {
            val intent = Intent(requireContext(), CreateCardActivity::class.java)
            startActivity(intent)
        }


        return view
    }
}
