package com.example.cartei_mobile_app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KartensatzDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kartensatz: Kartensatz): Long

    @Query("SELECT * FROM kartensätze")
     fun getAlleKartensätze(): LiveData<List<Kartensatz>>

    @Query("SELECT * FROM kartensätze WHERE titel = :titel LIMIT 1")
    fun getSatzMitTitel(titel: String): Kartensatz?

    @Delete
    fun deleteSatz(kartensatz: Kartensatz)

    @Query("SELECT * FROM kartensätze WHERE id = :id LIMIT 1")
    fun getSatzMitId(id: Int): Kartensatz?





}
