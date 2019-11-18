package com.autohub.skln.models.batchRequests

import android.os.Parcel
import android.os.Parcelable

data class GradeData(var grade: String? = null,
                     var color: String? = null,
                     var id: String? = null,
                     var image: String? = null,
                     var name: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

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

    companion object CREATOR : Parcelable.Creator<GradeData> {
        override fun createFromParcel(parcel: Parcel): GradeData {
            return GradeData(parcel)
        }

        override fun newArray(size: Int): Array<GradeData?> {
            return arrayOfNulls(size)
        }
    }
}
