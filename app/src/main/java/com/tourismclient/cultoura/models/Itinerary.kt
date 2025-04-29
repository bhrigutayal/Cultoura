package com.tourismclient.cultoura.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tourismclient.cultoura.database.Converters
import java.util.*

/**
 * Entity class representing an itinerary
 */
@Entity(tableName = "itineraries")
@TypeConverters(Converters::class)
data class Itinerary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val destination: String,
    val startDate: Date,
    val endDate: Date,
    val description: String = "",

    // List of activities or places for each day
    val dayPlans: List<DayPlan> = emptyList(),

    // Additional metadata
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isFavorite: Boolean = false,

    // Optional fields
    val coverImagePath: String? = null,
    val budget: Double? = null,
    val notes: String? = null,
    val tags: List<String> = emptyList()
)

/**
 * Data class representing activities planned for a specific day
 */
data class DayPlan(
    val dayNumber: Int,
    val date: Date,
    val activities: List<ActivityItem>,
    val title: String
)

/**
 * Data class representing a single activity in an itinerary
 */

/**
 * Enum representing different types of activities in an itinerary
 */
enum class ActivityType {
    ATTRACTION,
    RESTAURANT,
    HOTEL,
    TRANSPORTATION,
    SHOPPING,
    EVENT,
    OTHER
}