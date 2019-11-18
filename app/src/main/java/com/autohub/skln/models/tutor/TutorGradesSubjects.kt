package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable

data class TutorGradesSubjects(var id: String? = null,
                               var gradeId: String? = null,
                               var teacherId: String? = null,
                               var subjectId: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(gradeId)
        parcel.writeString(teacherId)
        parcel.writeString(subjectId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorGradesSubjects> {
        override fun createFromParcel(parcel: Parcel): TutorGradesSubjects {
            return TutorGradesSubjects(parcel)
        }

        override fun newArray(size: Int): Array<TutorGradesSubjects?> {
            return arrayOfNulls(size)
        }
    }
}
