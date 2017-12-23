package com.welter.guilherme.welterpneus

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.welter.guilherme.welterpneus.data.Pneu

/**
 * Created by guilherme on 22/12/17.
 */
class PneusListAdapter(val context: Context, val pneusList: List<Pneu>, val itemClick: (Pneu) -> Unit): RecyclerView.Adapter<PneusListAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindHolder(pneusList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.pneus_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return pneusList.size
    }

    inner class Holder(itemView: View?, val itemClick: (Pneu) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val pneuSize = itemView?.findViewById<TextView>(R.id.pneuSizeTextView)
        val pneuQuantity = itemView?.findViewById<TextView>(R.id.pneuQuantityTextView)

        fun bindHolder(pneu: Pneu) {
            pneuSize?.text = pneu.pneuSize.toString()
            pneuQuantity?.text = pneu.pneuQuantity.toString()

            itemView.setOnClickListener{ itemClick(pneu) }
        }
    }
}