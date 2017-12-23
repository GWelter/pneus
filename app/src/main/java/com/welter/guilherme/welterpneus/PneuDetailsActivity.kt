package com.welter.guilherme.welterpneus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CompoundButton
import com.welter.guilherme.welterpneus.adapters.PneusDetailsAdapter
import com.welter.guilherme.welterpneus.data.Constants
import com.welter.guilherme.welterpneus.data.DatabaseHelper
import kotlinx.android.synthetic.main.activity_pneu_details_acativity.*

class PneuDetailsActivity : AppCompatActivity() {

    private lateinit var pneuDetailsAdapter: PneusDetailsAdapter

    private fun setupAdapters() {
        pneuDetailsAdapter = PneusDetailsAdapter(this, Constants.pneuDetails)

        val layoutManager = LinearLayoutManager(this)
        pneusDetailsListView.layoutManager = layoutManager
        pneusDetailsListView.adapter = pneuDetailsAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pneu_details_acativity)

        val extras = intent.extras
        val tamanho = extras.getString("tamanho")

        var dbHelper = DatabaseHelper(this)
        dbHelper.queryDetailPeneus(tamanho)

        setupAdapters()

        supportActionBar?.title = "Pneus arro $tamanho"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        precoTotalDetailsEstoqueTextView.text = Constants.precoTotalEstoquePorTamanho.toString()
        exibirPrecoDetailsToggleButton.setOnCheckedChangeListener(exibirPrecoTotalChangeListener)

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReciever, IntentFilter(Constants.LIST_PNEU_BROADCAST))
    }

    private val exibirPrecoTotalChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if(isChecked) {
            precoTotalDetailsEstoqueTextView.visibility = View.VISIBLE
        } else {
            precoTotalDetailsEstoqueTextView.visibility = View.INVISIBLE
        }
    }

    private val broadcastReciever = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            pneuDetailsAdapter.notifyDataSetChanged()
            precoTotalDetailsEstoqueTextView.text = Constants.precoTotalEstoquePorTamanho.toString()
        }
    }
}
