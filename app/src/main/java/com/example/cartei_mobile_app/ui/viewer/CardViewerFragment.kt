package com.example.cartei_mobile_app.ui.viewer

import com.example.cartei_mobile_app.ui.main.LernActivity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.data.AppDatenbank
import com.example.cartei_mobile_app.data.Kartensatz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.cartei_mobile_app.ui.edit.SetBearbeitenActivity


class CardViewerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KartensetAdapter
    private var kartensets: List<Kartensatz> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        val view = inflater.inflate(R.layout.fragment_card_viewer, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewKartensets)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        parentFragmentManager.setFragmentResultListener("satzIdKey", viewLifecycleOwner) { _, bundle ->
            val satzId = bundle.getInt("satzId", 0)
            val modus = bundle.getString("modus", "bearbeiten")

            adapter = KartensetAdapter(kartensets) { ausgewählterSatz ->
                val intent = if (modus == "lernen") {
                    Intent(requireContext(), LernActivity::class.java) // ⚠️ deine Lern-Activity hier einsetzen!
                } else {
                    Intent(requireContext(), SetBearbeitenActivity::class.java)
                }
                intent.putExtra("satzId", ausgewählterSatz.id)
                startActivity(intent)
            }

            recyclerView.adapter = adapter
        }


        ladeKartensets()

        return view


    }

    private fun ladeKartensets() {
        val dao = AppDatenbank.getDatenbank(requireContext()).kartensatzDao()

        dao.getAlleKartensätze().observe(viewLifecycleOwner) { daten ->
            kartensets = daten
            adapter.updateListe(daten)
        }
    }

}

