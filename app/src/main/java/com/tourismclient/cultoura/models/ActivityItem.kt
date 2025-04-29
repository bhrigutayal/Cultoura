package com.tourismclient.cultoura.models

import android.os.Parcel
import android.os.Parcelable

data class ActivityItem(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: Int,
    val sectionId: Int,
    val duration: Int, 
    val cost: Double,
    val rating: Float,
    val location: Location? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readParcelable(Location::class.java.classLoader)
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeInt(imageUrl)
        dest.writeInt(sectionId)
        dest.writeInt(duration)
        dest.writeDouble(cost)
        dest.writeFloat(rating)
        dest.writeParcelable(location, flags)
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