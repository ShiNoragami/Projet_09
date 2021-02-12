package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.database.converters.PhotoDescriptionConverter
import com.openclassrooms.realestatemanager.database.converters.UriListConverter
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.models.Estate


@Database(entities = [(Estate::class)], version = 1, exportSchema = false)
@TypeConverters(UriListConverter::class, PhotoDescriptionConverter::class)
abstract class EstateDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun estateDao(): EstateDAO

    companion object {
        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: EstateDatabase? = null

        // --- INSTANCE ---
        fun getInstance(context: Context): EstateDatabase {
            if (INSTANCE == null) {
                synchronized(EstateDatabase::class.java) {

                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            EstateDatabase::class.java, "Estate.db")
                            .build()
                }
            }
            return INSTANCE as EstateDatabase
        }
    }
}