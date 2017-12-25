package com.welter.guilherme.welterpneus

import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.NumberPicker
import com.welter.guilherme.welterpneus.adapters.PneusDetailsAdapter
import com.welter.guilherme.welterpneus.data.Constants
import com.welter.guilherme.welterpneus.data.DatabaseHelper
import com.welter.guilherme.welterpneus.data.PneuDetails
import kotlinx.android.synthetic.main.activity_pneu_details_acativity.*

class PneuDetailsActivity : AppCompatActivity() {

    private lateinit var pneuDetailsAdapter: PneusDetailsAdapter
    private lateinit var pneuTamanho: String
    private var dbHelper = DatabaseHelper(this)

    private fun setupAdapters() {
        pneuDetailsAdapter = PneusDetailsAdapter(this, Constants.pneuDetails) { pneuDetails ->
            numberPickerDialaog(pneuDetails)
        }

        val layoutManager = LinearLayoutManager(this)
        pneusDetailsListView.layoutManager = layoutManager
        pneusDetailsListView.adapter = pneuDetailsAdapter
    }

    private fun numberPickerDialaog(pneuDetails: PneuDetails) {
        var removeQuantia = 1

        val numberPicker = NumberPicker(this)
        numberPicker.minValue = 1
        numberPicker.maxValue = pneuDetails.quantia
        numberPicker.setOnValueChangedListener({numberPicker, oldValue, newValue ->
            removeQuantia = newValue
        })

        val dialog: AlertDialog.Builder = AlertDialog.Builder(this).setView(numberPicker);
        dialog.setTitle("Remover do estoque")

        dialog.setPositiveButton("OK") { dialogInterface, i ->
            dbHelper.removePneus(pneuDetails.numeracao, pneuDetails.marca, removeQuantia)
            dbHelper.queryDetailPeneus(pneuTamanho)
            dbHelper.queryListaDePneus()

            if(Constants.pneuDetails.isEmpty()) {
                finish()
            }

        }
        dialog.setNegativeButton("Cancelar") {dialogInterface, i ->  }

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pneu_details_acativity)

        val extras = intent.extras
        val tamanho = extras.getString("tamanho")
        pneuTamanho = tamanho
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
