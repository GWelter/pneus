package com.welter.guilherme.welterpneus.data

import android.content.ContentValues
import android.content.Context

/**
 * Created by guilherme on 23/12/17.
 */
class DatabaseHelper(val context: Context) {
    private var pneusDatabase: DatabaseSchema.DatabaseCreation = (DatabaseSchema.DatabaseCreation(context))

    fun insertPneu(tamanho: String, marca: String, numeracao: String, preco: Float, quantia: Int) {
        val db = pneusDatabase.writableDatabase

        for (x in 0 until quantia) {
            val valores = ContentValues()
            valores.put(DatabaseSchema.TAMANHO, tamanho)
            valores.put(DatabaseSchema.MARCA, marca)
            valores.put(DatabaseSchema.NUMERACAO, numeracao)
            valores.put(DatabaseSchema.PRECO, preco)

            db.insert(DatabaseSchema.TABELA, null, valores)
        }
        db.close()
    }

    fun queryListaDePneus() {
        Constants.pneus.clear()
        val db = pneusDatabase.readableDatabase
        val cursor = db.rawQuery("Select tamanho, count(*) as quantia, sum(preco) as preco_total from pneus group by tamanho", null)

        if (cursor != null && cursor.count != 0) {
            var precoTotalEstoque = 0.0f
            cursor.moveToFirst()
            do {
                val tamanho = cursor.getInt(cursor.getColumnIndex("tamanho"))
                val quantia = cursor.getInt(cursor.getColumnIndex("quantia"))
                val precoTotal = cursor.getFloat(cursor.getColumnIndex("preco_total"))

                precoTotalEstoque += precoTotal
                println("Tamanho $tamanho, total $precoTotal")

                Constants.pneus.add(Pneu(tamanho, quantia))
            } while (cursor.moveToNext())
            Constants.precoTotalEstoque = precoTotalEstoque
        }
        db.close()
    }

    fun queryPneusPorTamanho() {
        val db = pneusDatabase.readableDatabase
    }
}