package com.example.cartei_mobile_app.ui.progress

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.data.Karte

class KartenAdapter(
    private var kartenListe: List<Karte>,
    private val onClick: ((Karte) -> Unit)? = null,
    private val onLongClick: ((Karte) -> Unit)? = null
) : RecyclerView.Adapter<KartenAdapter.KartenViewHolder>() {

    inner class KartenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val frageText: TextView = itemView.findViewById(R.id.text_karte_frage)
        val antwortText: TextView = itemView.findViewById(R.id.text_karte_antwort)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KartenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karte, parent, false)
        return KartenViewHolder(view)
    }

    override fun onBindViewHolder(holder: KartenViewHolder, position: Int) {
        val karte = kartenListe[position]
        holder.frageText.text = "Frage: ${karte.frage}"
        holder.antwortText.text = "Antwort: ${karte.antwort}"

        holder.itemView.setOnClickListener {
            onClick?.invoke(karte)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(karte)
            true
        }
    }

    override fun getItemCount() = kartenListe.size

    fun updateListe(neueKarten: List<Karte>) {
        kartenListe = neueKarten
        notifyDataSetChanged()
    }
}
