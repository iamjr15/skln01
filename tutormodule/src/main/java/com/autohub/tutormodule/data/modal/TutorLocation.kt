package com.autohub.tutormodule.data.modal

import android.os.Parcel
import android.os.Parcelable

data class TutorLocation(var geohash: String? = null,
                         var geopoint: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(geohash)
        parcel.writeString(geopoint)
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