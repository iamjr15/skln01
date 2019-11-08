package com.autohub.skln.models

import android.os.Parcel
import android.os.Parcelable

data class HobbiesData(val color: Int, val hobbyName: String?, val icon: Int, var selected: Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(color)
        parcel.writeString(hobbyName)
        parcel.writeInt(icon)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HobbiesData> {
        override fun createFromParcel(parcel: Parcel): HobbiesData {
            return HobbiesData(parcel)
        }

        override fun newArray(size: Int): Array<HobbiesData?> {
            return arrayOfNulls(size)
        }
    }


}