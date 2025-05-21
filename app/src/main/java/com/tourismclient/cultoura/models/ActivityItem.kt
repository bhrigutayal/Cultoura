package com.tourismclient.cultoura.models

import android.os.Parcel
import android.os.Parcelable

data class ActivityItem(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val sectionId: Int,
    val startHour : String,
    val endHour : String,
    val city : String,
    val date : String,
    val cost: Double,
    val rating: Float,
    val type : String,
    val location: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(imageUrl)
        dest.writeInt(sectionId)
        dest.writeString(startHour)
        dest.writeString(endHour)
        dest.writeString(city)
        dest.writeString(date)
        dest.writeDouble(cost)
        dest.writeFloat(rating)
        dest.writeString(type)
        dest.writeString(location)
    }


    companion object CREATOR : Parcelable.Creator<ActivityItem> {
        override fun createFromParcel(parcel: Parcel): ActivityItem {
            return ActivityItem(parcel)
        }

        override fun newArray(size: Int): Array<ActivityItem?> {
            return arrayOfNulls(size)
        }
    }
}