package com.welter.guilherme.welterpneus.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.welter.guilherme.welterpneus.R
import com.welter.guilherme.welterpneus.data.PneuDetails

/**
 * Created by guilherme on 23/12/17.
 */
class PneusDetailsAdapter(val context: Context, val pneuDetailList: List<PneuDetails>, val itemSellClick: (PneuDetails) -> Unit, val itemClick: (PneuDetails) -> Unit): RecyclerView.Adapter<PneusDetailsAdapter.PneuDetailHolder>() {
    override fun onBindViewHolder(holder: PneuDetailHolder?, position: Int) {
        holder?.bind(pneuDetailList[position])
    }

    override fun getItemCount(): Int {
        return pneuDetailList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PneuDetailHolder? {
        val view = LayoutInflater.from(context).inflate(R.layout.pneu_details_item, parent, false)
        return PneuDetailHolder(view, itemSellClick, itemClick)
    }

    inner class PneuDetailHolder(itemView: View?, val itemSellClick: (PneuDetails) -> Unit, val itemClick: (PneuDetails) -> Unit) : RecyclerView.ViewHolder(itemView) {

        val numeracaoTextView = itemView?.findViewById<TextView>(R.id.numeracaoDetailsTextView)
        val marcaTextView = itemView?.findViewById<TextView>(R.id.marcaDetailsTextView)
        val precoTextView = itemView?.findViewById<TextView>(R.id.precoDetailsTextView)
        val quantiaTextView = itemView?.findViewById<TextView>(R.id.quantiaDetailsTextView)
        val sellButton = itemView?.findViewById<ImageButton>(R.id.sellImageButton)

        fun bind(pneuDetail: PneuDetails) {
            numeracaoTextView?.text = pneuDetail.numeracao
            marcaTextView?.text = pneuDetail.marca
            precoTextView?.text = "R$ " + pneuDetail.preco.toString()
            quantiaTextView?.text = pneuDetail.quantia.toString()

            sellButton?.setOnClickListener { itemSellClick(pneuDetail) }
            itemView.setOnClickListener { itemClick(pneuDetail) }
        }
    }
}