package com.netzwelt.loginsignup.student.models

import android.os.Parcel
import android.os.Parcelable
/**
 * Created by Vt Netzwelt
 */
data class Classdata(var color: Int, var icon: Int, var selected: Boolean, var classname: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(color)
        parcel.writeInt(icon)
        parcel.writeByte(if (selected) 1 else 0)
        parcel.writeString(classname)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Classdata> {
        override fun createFromParcel(parcel: Parcel): Classdata {
            return Classdata(parcel)
        }

        override fun newArray(size: Int): Array<Classdata?> {
            return arrayOfNulls(size)
        }
    }
}