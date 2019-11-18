package com.autohub.skln.models.batchRequests

import android.os.Parcel
import android.os.Parcelable

data class SubjectData(var color: String? = null,
                       var id: String? = null,
                       var image: String? = null,
                       var name: String? = null,
                       var order: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(color)
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(order)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubjectData> {
        override fun createFromParcel(parcel: Parcel): SubjectData {
            return SubjectData(parcel)
        }

        override fun newArray(size: Int): Array<SubjectData?> {
            return arrayOfNulls(size)
        }
    }
}