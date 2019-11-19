package com.autohub

import android.os.Parcel
import android.os.Parcelable

data class SubjectsModel(var color: String? = "",
                         var id: String? = "",
                         var image: String? = "",
                         var name: String? = "",
                         var order: String? = "",
                         var bloccolor: Int = 0, var icon: Int = 0, var selected: Boolean = false


) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(color)
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(order)
        parcel.writeInt(bloccolor)
        parcel.writeInt(icon)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubjectsModel> {
        override fun createFromParcel(parcel: Parcel): SubjectsModel {
            return SubjectsModel(parcel)
        }

        override fun newArray(size: Int): Array<SubjectsModel?> {
            return arrayOfNulls(size)
        }
    }
}