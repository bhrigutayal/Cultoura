//package com.tourismclient.cultoura.repositories
//
//import com.tourismclient.cultoura.models.ActivityItem
//import com.tourismclient.cultoura.models.ActivityRequestDTO
//import com.tourismclient.cultoura.models.ActivitySection
//
//
//
//class ActivityRepository() {
//    // Get all activities
//
//
//    // Get activities by plan type
//    suspend fun getActivities(requestDTO: ActivityRequestDTO): Result<List<ActivitySection>> {
//        return try {
//            val response = activityApi.getAllActivities(requestDTO)
//            if (response.isSuccessful) {
//                Result.success(response.body() ?: emptyList())
//            } else {
//                Result.failure(Exception("Failed to fetch activities: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}