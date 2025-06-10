package com.example.cartei_mobile_app.ui.viewer

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cartei_mobile_app.R
import com.example.cartei_mobile_app.data.Kartensatz

class KartensetAdapter(
    private var sets: List<Kartensatz>,
    private val onClick: (Kartensatz) -> Unit
) : RecyclerView.Adapter<KartensetAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titelText: TextView = view.findViewById(R.id.text_kartenset_titel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kartenset, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val set = sets[position]
        holder.titelText.text = set.titel
        holder.itemView.setOnClickListener { onClick(set) }

    }

    override fun getItemCount() = sets.size

    fun updateListe(neu: List<Kartensatz>) {
        sets = neu
        notifyDataSetChanged()
    }
}
