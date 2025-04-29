package com.tourismclient.cultoura.repositories

import androidx.lifecycle.LiveData
import com.tourismclient.cultoura.models.Itinerary
import com.tourismclient.cultoura.database.ItineraryDao

class ItineraryRepository(private val itineraryDao: ItineraryDao) {

    /**
     * Gets all itineraries from the database
     * @return LiveData list of all itineraries
     */
    fun getAllItineraries(): LiveData<List<Itinerary>> {
        return itineraryDao.getAllItineraries()
    }

    /**
     * Gets a specific itinerary by ID
     * @param id The ID of the itinerary to retrieve
     * @return LiveData containing the requested itinerary
     */
    fun getItineraryById(id: Long): LiveData<Itinerary> {
        return itineraryDao.getItineraryById(id)
    }

    /**
     * Inserts a new itinerary into the database
     * @param itinerary The itinerary to be inserted
     * @return The ID of the inserted itinerary
     */
    suspend fun insertItinerary(itinerary: Itinerary): Long {
        return itineraryDao.insertItinerary(itinerary)
    }

    /**
     * Updates an existing itinerary in the database
     * @param itinerary The itinerary with updated information
     */
    suspend fun updateItinerary(itinerary: Itinerary) {
        itineraryDao.updateItinerary(itinerary)
    }

    /**
     * Deletes an itinerary from the database
     * @param itinerary The itinerary to be deleted
     */
    suspend fun deleteItinerary(itinerary: Itinerary) {
        itineraryDao.deleteItinerary(itinerary)
    }

    /**
     * Searches for itineraries by title or destination
     * @param query The search query string
     * @return LiveData list of matching itineraries
     */
    fun searchItineraries(query: String): LiveData<List<Itinerary>> {
        return itineraryDao.searchItineraries("%$query%")
    }
}