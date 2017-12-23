package com.welter.guilherme.welterpneus.data

/**
 * Created by guilherme on 23/12/17.
 */
object Constants {
    val pneus = ArrayList<Pneu>()
    val pneuDetails = ArrayList<PneuDetails>()
    val SAVE_PNEU_BROADCAST: String = "save broadcast"
    val LIST_PNEU_BROADCAST: String = "remove broadcast"

    var precoTotalEstoque = 0.0f
    var precoTotalEstoquePorTamanho = 0.0f
}