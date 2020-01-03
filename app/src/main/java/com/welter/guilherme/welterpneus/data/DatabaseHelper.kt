package com.welter.guilherme.welterpneus.data

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager


/**
 * Created by guilherme on 23/12/17.
 */
class DatabaseHelper(val context: Context) {
    private var pneusDatabase: DatabaseSchema.DatabaseCreation = (DatabaseSchema.DatabaseCreation(context))

    fun insertPneu(tamanho: String, marca: String, numeracao: String, preco: Float, quantia: Int) {
        removePneus(numeracao, marca, null)
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
        queryDetailPeneusBySize(tamanho)
        queryListaDePneus()
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

                Constants.pneus.add(Pneu(tamanho, quantia))
            } while (cursor.moveToNext())
            Constants.precoTotalEstoque = precoTotalEstoque
        }

        val broadcast = Intent(Constants.SAVE_PNEU_BROADCAST)
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcast)

        db.close()
    }

    fun queryDetailPeneusBySize(tamanho: String) {
        Constants.pneuDetails.clear()
        var db = pneusDatabase.readableDatabase

        val query = "select ${DatabaseSchema.NUMERACAO},"+
                    "${DatabaseSchema.MARCA},"+
                    "${DatabaseSchema.PRECO},"+
                    "sum(${DatabaseSchema.PRECO}) as preco_total,"+
                    "count(*) as quantia "+
                    "from ${DatabaseSchema.TABELA} where ${DatabaseSchema.TAMANHO} = '$tamanho' "+
                    "group by ${DatabaseSchema.NUMERACAO},${DatabaseSchema.MARCA}"

        val cursor = db.rawQuery(query, null)

        if (cursor != null && cursor.count != 0) {
            var precoTotalEstoque = 0.0f
            cursor.moveToFirst()
            do {
                val numeracao = cursor.getString(cursor.getColumnIndex(DatabaseSchema.NUMERACAO))
                val marca = cursor.getString(cursor.getColumnIndex(DatabaseSchema.MARCA))
                val preco = cursor.getFloat(cursor.getColumnIndex(DatabaseSchema.PRECO))
                val precoTotal = cursor.getFloat(cursor.getColumnIndex("preco_total"))
                val quantia = cursor.getInt(cursor.getColumnIndex("quantia"))

                precoTotalEstoque += precoTotal
                Constants.pneuDetails.add(PneuDetails(numeracao, marca, preco, quantia))

            } while (cursor.moveToNext())
            Constants.precoTotalEstoquePorTamanho = precoTotalEstoque

            val broadcast = Intent(Constants.LIST_PNEU_BROADCAST)
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcast)
        }
        db.close()
    }

    fun removePneus(numeracao: String, marca: String, quantia: Int?) {
        var db = pneusDatabase.readableDatabase

        val removeQuery = "DELETE from ${DatabaseSchema.TABELA} "+
                        "WHERE ${DatabaseSchema.ID} IN "+
                        "(SELECT ${DatabaseSchema.ID} from ${DatabaseSchema.TABELA} "+
                        "WHERE ${DatabaseSchema.NUMERACAO} = '$numeracao' "+
                        "AND ${DatabaseSchema.MARCA} = '$marca' "+
                        if (quantia !== null) "LIMIT $quantia)" else ")"

        println(removeQuery)

        db.execSQL(removeQuery)
        db.close()
    }
}