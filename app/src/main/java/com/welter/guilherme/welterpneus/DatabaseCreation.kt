package com.welter.guilherme.welterpneus

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by guilherme on 22/12/17.
 */
class DatabaseCreation(context: Context?)
    : SQLiteOpenHelper(context, "pneus.db", null, 1) {

    val TABELA: String = "pneus"
    val ID: String = "_id"
    val TAMANHO: String = "tamanho"
    val MARCA: String = "marca"
    val NUMERACAO: String = "numeracao"
    val PRECO: String = "preco"

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreation: String = "CREATE TABLE {$TABELA} (" +
                                    "${ID} integer primary key autoincrement," +
                                    "${TAMANHO} text," +
                                    "${MARCA} text," +
                                    "${NUMERACAO} text," +
                                    "${PRECO} integer)"

        db?.execSQL(sqlCreation)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABELA}")
        onCreate(db)
    }
}