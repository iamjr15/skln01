package com.autohub.skln.models.batchRequests

import android.os.Parcel
import android.os.Parcelable

data class StudentData(var id: String? = "",
                       var name: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentData> {
        override fun createFromParcel(parcel: Parcel): StudentData {
            return StudentData(parcel)
        }

        override fun newArray(size: Int): Array<StudentData?> {
            return arrayOfNulls(size)
        }
    }
}