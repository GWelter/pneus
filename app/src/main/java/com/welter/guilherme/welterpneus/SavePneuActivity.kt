package com.welter.guilherme.welterpneus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SavePneuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_pneu)

        supportActionBar?.title = "Adicionar Pneu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
