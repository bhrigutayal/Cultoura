package com.tourismclient.cultoura.models

data class ItineraryItem(
    val id: Int,
    val title: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val cost: Int,
    val imageResId: Int
)
