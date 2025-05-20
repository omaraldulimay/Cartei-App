package com.example.cartei_mobile_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Karte::class, Kartensatz::class], version = 2, exportSchema = false)
abstract class AppDatenbank : RoomDatabase() {

    abstract fun kartenDao(): KartenDao
    abstract fun kartensatzDao(): KartensatzDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatenbank? = null

        fun getDatenbank(context: Context): AppDatenbank {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatenbank::class.java,
                    "cartei_datenbank"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
