package com.tourismclient.cultoura.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourismclient.cultoura.models.Itinerary
import com.tourismclient.cultoura.repositories.ItineraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItineraryViewModel : ViewModel(){

    private val repository: ItineraryRepository = TODO()


    /**
     * Gets all itineraries from the database
     * @return LiveData list of all itineraries
     */
    fun getAllItineraries(): LiveData<List<Itinerary>> {
        return repository.getAllItineraries()
    }

    /**
     * Gets a specific itinerary by ID
     * @param id The ID of the itinerary to retrieve
     * @return LiveData containing the requested itinerary
     */
    fun getItineraryById(id: Long): LiveData<Itinerary> {
        return repository.getItineraryById(id)
    }

    /**
     * Inserts a new itinerary into the database
     * @param itinerary The itinerary to be inserted
     */
    fun insertItinerary(itinerary: Itinerary) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItinerary(itinerary)
        }
    }

    /**
     * Updates an existing itinerary in the database
     * @param itinerary The itinerary with updated information
     */
    fun updateItinerary(itinerary: Itinerary) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItinerary(itinerary)
        }
    }

    /**
     * Deletes an itinerary from the database
     * @param itinerary The itinerary to be deleted
     */
    fun deleteItinerary(itinerary: Itinerary) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItinerary(itinerary)
        }
    }

    /**
     * Searches for itineraries by title or destination
     * @param query The search query string
     * @return LiveData list of matching itineraries
     */
    fun searchItineraries(query: String): LiveData<List<Itinerary>> {
        return repository.searchItineraries(query)
    }
}
