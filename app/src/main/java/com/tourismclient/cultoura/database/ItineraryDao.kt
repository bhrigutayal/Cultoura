package com.tourismclient.cultoura.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tourismclient.cultoura.models.Itinerary

/**
 * Data Access Object for Itinerary entity
 * Provides methods to query, insert, update, and delete itineraries in the database
 */
@Dao
interface ItineraryDao {
    /**
     * Gets all itineraries from database in descending order by creation date
     * @return LiveData list of all itineraries
     */
    @Query("SELECT * FROM itineraries ORDER BY createdAt DESC")
    fun getAllItineraries(): LiveData<List<Itinerary>>

    /**
     * Gets a specific itinerary by ID
     * @param id The ID of the itinerary to retrieve
     * @return LiveData containing the requested itinerary
     */
    @Query("SELECT * FROM itineraries WHERE id = :id")
    fun getItineraryById(id: Long): LiveData<Itinerary>

    /**
     * Inserts a new itinerary into the database
     * @param itinerary The itinerary to be inserted
     * @return The row ID of the newly inserted itinerary
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItinerary(itinerary: Itinerary): Long

    /**
     * Updates an existing itinerary in the database
     * @param itinerary The itinerary with updated information
     */
    @Update
    suspend fun updateItinerary(itinerary: Itinerary)

    /**
     * Deletes an itinerary from the database
     * @param itinerary The itinerary to be deleted
     */
    @Delete
    suspend fun deleteItinerary(itinerary: Itinerary)

    /**
     * Searches for itineraries by title or destination
     * @param query The search query string with wildcards (%)
     * @return LiveData list of matching itineraries
     */
    @Query("SELECT * FROM itineraries WHERE title LIKE :query OR destination LIKE :query ORDER BY createdAt DESC")
    fun searchItineraries(query: String): LiveData<List<Itinerary>>

    /**
     * Gets count of all itineraries in the database
     * @return Number of itineraries
     */
    @Query("SELECT COUNT(*) FROM itineraries")
    fun getItineraryCount(): LiveData<Int>

    /**
     * Gets itineraries for a specific destination
     * @param destination The destination to filter by
     * @return LiveData list of itineraries for the specified destination
     */
    @Query("SELECT * FROM itineraries WHERE destination = :destination ORDER BY createdAt DESC")
    fun getItinerariesByDestination(destination: String): LiveData<List<Itinerary>>

    /**
     * Deletes all itineraries from the database
     * Use with caution
     */
    @Query("DELETE FROM itineraries")
    suspend fun deleteAllItineraries()
}