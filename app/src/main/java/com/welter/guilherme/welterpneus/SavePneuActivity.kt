package com.welter.guilherme.welterpneus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.welter.guilherme.welterpneus.data.Constants
import com.welter.guilherme.welterpneus.data.DatabaseHelper
import kotlinx.android.synthetic.main.activity_save_pneu.*

class SavePneuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_pneu)

        supportActionBar?.title = "Adicionar Pneu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun salvarPneus(view: View) {
        val dbHelper = DatabaseHelper(this)

        val tamanho = tamanhoEditText.text.toString()
        val marca = marcaEditText.text.toString()
        val numeracao = numeracaoEditText.text.toString()
        val preco = precoEditText.text.toString()
        val quantia = quantiaEditText.text.toString()

        if(
        tamanho.isNotEmpty() && tamanho.isNotBlank() &&
        marca.isNotEmpty() && marca.isNotBlank() &&
        numeracao.isNotEmpty() && numeracao.isNotBlank() &&
        preco.isNotEmpty() && preco.isNotBlank() &&
        quantia.isNotEmpty() && quantia.isNotBlank()) {

            dbHelper.insertPneu(tamanho, marca, numeracao, preco.toFloat(), quantia.toInt())
            dbHelper.queryListaDePneus()

            finish()
        }
    }
}
