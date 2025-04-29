package com.tourismclient.cultoura.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tourismclient.cultoura.models.DayPlan
import java.util.*

/**
 * Type converters for Room database to handle complex types
 */
class Converters {
    private val gson = Gson()

    // Date converters
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // DayPlan list converters
    @TypeConverter
    fun fromDayPlanList(value: List<DayPlan>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDayPlanList(value: String): List<DayPlan> {
        val listType = object : TypeToken<List<DayPlan>>() {}.type
        return gson.fromJson(value, listType)
    }

    // String list converters (for tags)
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}
