package com.tourismclient.cultoura.models

data class ActivitySection(
    val id: Long,
    val title: String,
    val activities: List<ActivityItem>
)
