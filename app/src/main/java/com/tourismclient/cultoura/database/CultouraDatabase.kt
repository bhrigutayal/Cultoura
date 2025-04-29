package com.tourismclient.cultoura.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tourismclient.cultoura.models.Itinerary

/**
 * Main database class for the Cultoura app
 */
@Database(
    entities = [Itinerary::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CultouraDatabase : RoomDatabase() {

    /**
     * Provides access to the Itinerary DAO
     */
    abstract fun itineraryDao(): ItineraryDao

    companion object {
        @Volatile
        private var INSTANCE: CultouraDatabase? = null

        /**
         * Gets the singleton database instance
         */
        fun getDatabase(context: Context): CultouraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CultouraDatabase::class.java,
                    "cultoura_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}