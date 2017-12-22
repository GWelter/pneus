package com.welter.guilherme.welterpneus

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_pneus_list.*
import kotlinx.android.synthetic.main.content_pneus_list.*

class PneusListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pneus_list)
        toolbar.title = "Welter Pneus"
        setSupportActionBar(toolbar)

        var pneuList = listOf<Pneu>(
                Pneu(13, 2),
                Pneu(14, 3),
                Pneu(15, 5),
                Pneu(16, 4),
                Pneu(17, 6)
        )

        val pneuAdapter = PneusListAdapter(this, pneuList) { pneu ->
            Toast.makeText(this, pneu.pneuSize.toString(), Toast.LENGTH_SHORT).show()
        }

        val layoutManager = LinearLayoutManager(this)

        pneusListView2.layoutManager = layoutManager
        pneusListView2.adapter = pneuAdapter

        fab.setOnClickListener { _ ->
            val intent = Intent(this, SavePneuActivity::class.java)
            startActivity(intent)
        }
    }

}
