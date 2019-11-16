package com.autohub.skln.models

import android.os.Parcel
import android.os.Parcelable

data class GradesData(var grade: String? = null,
                      var color: String? = null,
                      var id: String? = null,
                      var image: String? = null,
                      var name: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(grade)
        parcel.writeString(color)
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GradesData> {
        override fun createFromParcel(parcel: Parcel): GradesData {
            return GradesData(parcel)
        }

        override fun newArray(size: Int): Array<GradesData?> {
            return arrayOfNulls(size)
        }
    }
}
