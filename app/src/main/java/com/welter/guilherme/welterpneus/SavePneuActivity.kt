package com.welter.guilherme.welterpneus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.View
import com.welter.guilherme.welterpneus.data.Constants
import com.welter.guilherme.welterpneus.data.DatabaseHelper
import kotlinx.android.synthetic.main.activity_save_pneu.*

class SavePneuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_pneu)

        val extras = intent.extras
        loadParams(extras)

        supportActionBar?.title = "Adicionar Pneu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadParams(extras: Bundle?) {
        val tamanho = extras?.getString("PNEU_TAMANHO", "")
        val numeracao = extras?.getString("PNEU_NUMERACAO", "")
        val marca = extras?.getString("PNEU_MARCA", "")
        val preco = extras?.getFloat("PNEU_PRECO", 0.0f)
        val quantia = extras?.getInt("PNEU_QUANTIA", 0)

        tamanhoEditText.setText(tamanho)
        numeracaoEditText.setText(numeracao)
        marcaEditText.setText(marca)
        precoEditText.setText(preco.toString())
        quantiaEditText.setText(quantia.toString())
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
