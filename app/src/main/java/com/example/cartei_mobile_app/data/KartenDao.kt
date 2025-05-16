package com.example.cartei_mobile_app.data


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KartenDao {

    @Query("SELECT * FROM karten WHERE satzId = :satzId")
    fun alleKartenFürSatz(satzId: Int): LiveData<List<Karte>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun karteEinfügen(karte: Karte)

    @Update
     fun karteAktualisieren(karte: Karte)

    @Delete
    suspend fun karteLöschen(karte: Karte)


    @Query("DELETE FROM karten WHERE satzId = :satzId")
    fun alleKartenEinesSatzesLöschen(satzId: Int)

}
