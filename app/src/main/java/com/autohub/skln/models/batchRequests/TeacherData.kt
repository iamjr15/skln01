package com.autohub.skln.models.batchRequests

import android.os.Parcel
import android.os.Parcelable

class TeacherData(var id: String? = null,
                  var name: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TeacherData> {
        override fun createFromParcel(parcel: Parcel): TeacherData {
            return TeacherData(parcel)
        }

        override fun newArray(size: Int): Array<TeacherData?> {
            return arrayOfNulls(size)
        }
    }
}