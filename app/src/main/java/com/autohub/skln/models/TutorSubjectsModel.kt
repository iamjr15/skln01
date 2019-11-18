package com.autohub.skln.models

import android.os.Parcel
import android.os.Parcelable

data class TutorSubjectsModel(var id: String? ="",var subjectId: String? =""
                              ,var teacherId: String? ="" ) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(subjectId)
        parcel.writeString(teacherId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorSubjectsModel> {
        override fun createFromParcel(parcel: Parcel): TutorSubjectsModel {
            return TutorSubjectsModel(parcel)
        }

        override fun newArray(size: Int): Array<TutorSubjectsModel?> {
            return arrayOfNulls(size)
        }
    }
}