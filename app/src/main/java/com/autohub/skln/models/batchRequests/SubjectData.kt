package com.autohub.skln.models.batchRequests

import android.os.Parcel
import android.os.Parcelable

data class SubjectData(var color: String? = "",
                       var id: String? = "",
                       var grades: Grades? = Grades(),
                       var image: String? = "",
                       var name: String? = "",
                       var isFavSelected: Boolean? = false,
                       var isleastelected: Boolean? = false,
                       var isHobbySelected: Boolean? = false,
                       var order: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Grades::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(color)
        parcel.writeString(id)
        parcel.writeParcelable(grades, flags)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeValue(isFavSelected)
        parcel.writeValue(isleastelected)
        parcel.writeValue(isHobbySelected)
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


data class Grades(var grade11to12: Boolean = false, var grade1to5: Boolean = false, var grade6to10: Boolean = false,
                  var hobbies: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (grade11to12) 1 else 0)
        parcel.writeByte(if (grade1to5) 1 else 0)
        parcel.writeByte(if (grade6to10) 1 else 0)
        parcel.writeByte(if (hobbies) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Grades> {
        override fun createFromParcel(parcel: Parcel): Grades {
            return Grades(parcel)
        }

        override fun newArray(size: Int): Array<Grades?> {
            return arrayOfNulls(size)
        }
    }
}


