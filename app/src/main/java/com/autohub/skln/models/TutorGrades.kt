package com.autohub.skln.models

import android.os.Parcel
import android.os.Parcelable

data class TutorGrades(var gradeId: String? = "", var id: String? = "", var teacherId: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gradeId)
        parcel.writeString(id)
        parcel.writeString(teacherId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorGrades> {
        override fun createFromParcel(parcel: Parcel): TutorGrades {
            return TutorGrades(parcel)
        }

        override fun newArray(size: Int): Array<TutorGrades?> {
            return arrayOfNulls(size)
        }
    }
}