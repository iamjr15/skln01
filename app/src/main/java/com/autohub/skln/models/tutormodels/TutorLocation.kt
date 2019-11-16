package com.autohub.skln.models.tutormodels

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class TutorLocation(var geohash: String? = "",
                         var geopoint: GeoPoint? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            TODO("geopoint")) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(geohash)
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