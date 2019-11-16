package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable

data class TutorQualification(var belongToInstitute: Boolean? = null,
                              var classType: String? = null,
                              var currentOccupation: String? = null,
                              var experience: String? = null,
                              var instituteNeme: String? = null,
                              var numberOfStudents: String? = null,
                              var qualification: String? = null,
                              var qualificationArea: String? = null,
                              var roleInInstitute: String? = null,
                              var targetBoard: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(belongToInstitute)
        parcel.writeString(classType)
        parcel.writeString(currentOccupation)
        parcel.writeString(experience)
        parcel.writeString(instituteNeme)
        parcel.writeString(numberOfStudents)
        parcel.writeString(qualification)
        parcel.writeString(qualificationArea)
        parcel.writeString(roleInInstitute)
        parcel.writeString(targetBoard)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorQualification> {
        override fun createFromParcel(parcel: Parcel): TutorQualification {
            return TutorQualification(parcel)
        }

        override fun newArray(size: Int): Array<TutorQualification?> {
            return arrayOfNulls(size)
        }
    }

}

