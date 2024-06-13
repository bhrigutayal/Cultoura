package com.example.tourismapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ItenaryItem(
    val date:String,
    val event:String,
    val location:String,
    val time:String,
    val about:String,
    val NumberofPeople:String,
    val FriendorFamily:String,
    val State:String,
    val Price:String,
    val image: String = ""

): Parcelable

data class ItemsResponse(val categories: List<ItenaryItem>)
