package com.tourismclient.cultoura.models

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val distance: String,
    val imageUrl: String,
    val isFeatured: Boolean,
    var isSaved: Boolean = false
)