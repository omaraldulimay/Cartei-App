package com.example.cartei.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KartenDao {

    @Query("SELECT * FROM karten WHERE satzId = :satzId")
    fun alleKartenFürSatz(satzId: Int): LiveData<List<Karte>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun karteEinfügen(karte: Karte)

    @Update
    suspend fun karteAktualisieren(karte: Karte)

    @Delete
    suspend fun karteLöschen(karte: Karte)

    @Query("DELETE FROM karten WHERE satzId = :satzId")
    suspend fun alleKartenEinesSatzesLöschen(satzId: Int)
}
