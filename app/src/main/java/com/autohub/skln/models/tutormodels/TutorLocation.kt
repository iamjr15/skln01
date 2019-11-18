package com.autohub.skln.models.tutormodels

import android.os.Parcel
import android.os.Parcelable

data class TutorLocation(var geohash: String? = "",
                         var latitude: Double? = 0.0,
                         var longitude: Double? = 0.0) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(geohash)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorLocation> {
        override fun createFromParcel(parcel: Parcel): TutorLocation {
            return TutorLocation(parcel)
        }

        override fun newArray(size: Int): Array<TutorLocation?> {
            return arrayOfNulls(size)
        }
    }
}
