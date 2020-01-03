package com.welter.guilherme.welterpneus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.CompoundButton
import com.welter.guilherme.welterpneus.adapters.PneusListAdapter
import com.welter.guilherme.welterpneus.data.Constants
import com.welter.guilherme.welterpneus.data.DatabaseHelper
import com.welter.guilherme.welterpneus.data.Pneu

import kotlinx.android.synthetic.main.activity_pneus_list.*
import kotlinx.android.synthetic.main.content_pneus_list.*

class PneusListActivity : AppCompatActivity() {


    private lateinit var pneuAdapter: PneusListAdapter

    private fun setupAdapters() {
        pneuAdapter = PneusListAdapter(this, Constants.pneus as List<Pneu>) { pneu ->
            val intent = Intent(this, PneuDetailsActivity::class.java)
            intent.putExtra("tamanho", pneu.pneuSize.toString())
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)

        pneusListView2.layoutManager = layoutManager
        pneusListView2.adapter = pneuAdapter

        val itemDecoration = DividerItemDecoration(this, ClipDrawable.HORIZONTAL)
        pneusListView2.addItemDecoration(itemDecoration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pneus_list)
        toolbar.title = "Welter Pneus"
        setSupportActionBar(toolbar)

        var dbHelper = DatabaseHelper(this)
        dbHelper.queryListaDePneus()

        setupAdapters()

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReciever, IntentFilter(Constants.SAVE_PNEU_BROADCAST))

        fab.setOnClickListener { _ ->
            val intent = Intent(this, SavePneuActivity::class.java)
            startActivity(intent)
        }

        precoTotalEstoqueTextView.text = Constants.precoTotalEstoque.toString()
        exibirPrecoToggleButton.setOnCheckedChangeListener(exibirPrecoTotalChangeListener)
    }

    private val exibirPrecoTotalChangeListener = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if(isChecked) {
                precoTotalEstoqueTextView.visibility = View.VISIBLE
            } else {
                precoTotalEstoqueTextView.visibility = View.INVISIBLE
            }
        }
    }

    private val broadcastReciever = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(Constants.pneus.isEmpty()){
                Constants.precoTotalEstoque = 0f
            }
            pneuAdapter.notifyDataSetChanged()
            precoTotalEstoqueTextView.text = Constants.precoTotalEstoque.toString()
        }
    }

}
