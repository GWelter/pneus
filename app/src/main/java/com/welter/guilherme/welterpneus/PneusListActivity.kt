package com.welter.guilherme.welterpneus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.CompoundButtonCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.welter.guilherme.welterpneus.data.Constants
import com.welter.guilherme.welterpneus.data.DatabaseHelper

import kotlinx.android.synthetic.main.activity_pneus_list.*
import kotlinx.android.synthetic.main.content_pneus_list.*

class PneusListActivity : AppCompatActivity() {


    private lateinit var pneuAdapter: PneusListAdapter

    private fun setupAdapters() {
        pneuAdapter = PneusListAdapter(this, Constants.pneus) { pneu ->
            Toast.makeText(this, pneu.pneuSize.toString(), Toast.LENGTH_SHORT).show()
        }

        val layoutManager = LinearLayoutManager(this)

        pneusListView2.layoutManager = layoutManager
        pneusListView2.adapter = pneuAdapter
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
            println("Recebeu")
            pneuAdapter.notifyDataSetChanged()
            precoTotalEstoqueTextView.text = Constants.precoTotalEstoque.toString()
        }
    }

}
