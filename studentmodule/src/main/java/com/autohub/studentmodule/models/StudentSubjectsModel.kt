package com.autohub.studentmodule.models

import android.os.Parcel
import android.os.Parcelable

data class StudentSubjectsModel(var id: String? = "", @field:JvmField var isAcademic: Boolean? = false,
                                @field:JvmField var isFavourite: Boolean? = false,
                                @field:JvmField var isHobby: Boolean? = false,
                                @field:JvmField var isLeastFavourite: Boolean? = false,
                                var subjectName: String? = "", var studentId: String? = "",
                                var subjectId: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeValue(isAcademic)
        parcel.writeValue(isFavourite)
        parcel.writeValue(isHobby)
        parcel.writeValue(isLeastFavourite)
        parcel.writeString(subjectName)
        parcel.writeString(studentId)
        parcel.writeString(subjectId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentSubjectsModel> {
        override fun createFromParcel(parcel: Parcel): StudentSubjectsModel {
            return StudentSubjectsModel(parcel)
        }

        override fun newArray(size: Int): Array<StudentSubjectsModel?> {
            return arrayOfNulls(size)
        }
    }
}