package com.welter.guilherme.welterpneus

import android.content.*
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
        pneuDetailsAdapter = PneusDetailsAdapter(this, Constants.pneuDetails, { sellItem(it) }, { editItem(it) })

        val layoutManager = LinearLayoutManager(this)
        pneusDetailsListView.layoutManager = layoutManager
        pneusDetailsListView.adapter = pneuDetailsAdapter

        val itemDecoration = DividerItemDecoration(this, HORIZONTAL)
        pneusDetailsListView.addItemDecoration(itemDecoration)
    }

    private fun editItem(pneuDetails: PneuDetails) {
        val intent = Intent(this, SavePneuActivity::class.java)
        intent.putExtra("PNEU_TAMANHO", pneuTamanho)
        intent.putExtra("PNEU_MARCA", pneuDetails.marca)
        intent.putExtra("PNEU_NUMERACAO", pneuDetails.numeracao)
        intent.putExtra("PNEU_PRECO", pneuDetails.preco)
        intent.putExtra("PNEU_QUANTIA", pneuDetails.quantia)
        startActivity(intent)
    }

    private fun sellItem(pneuDetails: PneuDetails) {
        var removeQuantia = 1

        val numberPicker = NumberPicker(this)
        numberPicker.minValue = 1
        numberPicker.maxValue = pneuDetails.quantia
        numberPicker.setOnValueChangedListener { _, _, newValue ->
            removeQuantia = newValue
        }

        val dialog: AlertDialog.Builder = AlertDialog.Builder(this).setView(numberPicker);
        dialog.setTitle("Remover do estoque")

        dialog.setPositiveButton("OK") { _, _ ->
            dbHelper.removePneus(pneuDetails.numeracao, pneuDetails.marca, removeQuantia)
            dbHelper.queryDetailPeneusBySize(pneuTamanho)
            dbHelper.queryListaDePneus()

            if(Constants.pneuDetails.isEmpty()) {
                finish()
            }

        }
        dialog.setNegativeButton("Cancelar") {_, _ ->  }

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pneu_details_acativity)

        val extras = intent.extras
        val tamanho = extras!!.getString("tamanho", "")
        pneuTamanho = tamanho
        dbHelper.queryDetailPeneusBySize(tamanho)

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
