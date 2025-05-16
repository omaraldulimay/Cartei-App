package com.example.cartei_mobile_app.repository

import androidx.lifecycle.LiveData
import com.example.cartei_mobile_app.data.Karte
import com.example.cartei_mobile_app.data.KartenDao

class KartenRepository(private val dao: KartenDao) {

    fun kartenFürSatz(satzId: Int): LiveData<List<Karte>> {
        return dao.alleKartenFürSatz(satzId)
    }

    suspend fun karteEinfügen(karte: Karte) {
        dao.karteEinfügen(karte)
    }

    suspend fun karteAktualisieren(karte: Karte) {
        dao.karteAktualisieren(karte)
    }

    suspend fun karteLöschen(karte: Karte) {
        dao.karteLöschen(karte)
    }

    suspend fun kartenEinesSatzesLöschen(satzId: Int) {
        dao.alleKartenEinesSatzesLöschen(satzId)
    }
}
