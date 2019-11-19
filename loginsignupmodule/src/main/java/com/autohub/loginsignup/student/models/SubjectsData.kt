package com.autohub.loginsignup.student.models

import android.os.Parcel
import android.os.Parcelable
/**
 * Created by Vt Netzwelt
 */
data class SubjectsData(var color: Int, var icon: Int, var selected: Boolean, var subjectName: String?


) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(color)
        parcel.writeInt(icon)
        parcel.writeByte(if (selected) 1 else 0)
        parcel.writeString(subjectName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubjectsData> {
        override fun createFromParcel(parcel: Parcel): SubjectsData {
            return SubjectsData(parcel)
        }

        override fun newArray(size: Int): Array<SubjectsData?> {
            return arrayOfNulls(size)
        }
    }
}