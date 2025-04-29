//package com.tourismclient.cultoura.repositories
//
//import com.tourismclient.cultoura.models.Destination
//import com.tourismclient.cultoura.models.DestinationDetails
//
//import com.tourismclient.cultoura.utils.Result
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class DestinationRepository(private val apiService: ApiService) {
//
//    suspend fun getAllDestinations(): Result<List<Destination>> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = apiService.getAllDestinations()
//                if (response.isSuccessful && response.body() != null) {
//                    Result.Success(response.body()!!)
//                } else {
//                    Result.Error("Failed to fetch destinations: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Result.Error("Network error: ${e.message}")
//            }
//        }
//    }
//
//    suspend fun getDestinationDetails(destinationId: String): Result<DestinationDetails> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = apiService.getDestinationDetails(destinationId)
//                if (response.isSuccessful && response.body() != null) {
//                    Result.Success(response.body()!!)
//                } else {
//                    Result.Error("Failed to fetch destination details: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Result.Error("Network error: ${e.message}")
//            }
//        }
//    }
//}