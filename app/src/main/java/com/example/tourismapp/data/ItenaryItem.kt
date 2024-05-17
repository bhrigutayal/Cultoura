package com.example.tourismapp.data





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

)

data class ItemsResponse(val items: List<ItenaryItem>)
