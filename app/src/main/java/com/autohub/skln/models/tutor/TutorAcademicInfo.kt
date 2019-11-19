package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class TutorAcademicInfo(var belongToInstitute : Boolean? = false,
                             var classType: ArrayList<String>? = arrayListOf(),
                             var  instituteNeme : String? = "",
                             var  numberOfStudents : String? = "",
                             var  roleInInstitute : String? = "",
                             var  selectedCategory : String? = "",
                             var  teachingMethod : String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            TODO("classType"),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(belongToInstitute)
        parcel.writeString(instituteNeme)
        parcel.writeString(numberOfStudents)
        parcel.writeString(roleInInstitute)
        parcel.writeString(selectedCategory)
        parcel.writeString(teachingMethod)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorAcademicInfo> {
        override fun createFromParcel(parcel: Parcel): TutorAcademicInfo {
            return TutorAcademicInfo(parcel)
        }

        override fun newArray(size: Int): Array<TutorAcademicInfo?> {
            return arrayOfNulls(size)
        }
    }
}