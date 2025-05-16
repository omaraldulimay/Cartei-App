package com.example.cartei_mobile_app.ui.viewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.data.Karte
import android.content.Intent
import com.example.cartei_mobile_app.ui.create.CreateCardActivity


class KartenAdapter(
    private val kartenListe: List<Karte>,
    private val onLongClick: (Karte) -> Unit
) : RecyclerView.Adapter<KartenAdapter.KartenViewHolder>() {

    inner class KartenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frageText: TextView = view.findViewById(R.id.text_frage)
        val antwortText: TextView = view.findViewById(R.id.text_antwort)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KartenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karte, parent, false)
        return KartenViewHolder(view)
    }

    override fun onBindViewHolder(holder: KartenViewHolder, position: Int) {
        val karte = kartenListe[position]

        holder.frageText.text = karte.frage
        holder.antwortText.text = karte.antwort

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CreateCardActivity::class.java).apply {
                putExtra("karteId", karte.id)
                putExtra("frage", karte.frage)
                putExtra("antwort", karte.antwort)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = kartenListe.size
}
