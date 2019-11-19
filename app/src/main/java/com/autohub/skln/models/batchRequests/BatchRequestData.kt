package com.autohub.skln.models.batchRequests

import android.os.Parcel
import android.os.Parcelable

class BatchRequestData(var documentId: String? = null,
                       var id: String? = null,
                       var status: String? = null,
                       var grade: GradeData? = null,
                       var student: StudentData? = null,
                       var subject: SubjectData? = null,
                       var teacher: TeacherData? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(GradeData::class.java.classLoader),
            parcel.readParcelable(StudentData::class.java.classLoader),
            parcel.readParcelable(SubjectData::class.java.classLoader),
            parcel.readParcelable(TeacherData::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(documentId)
        parcel.writeString(id)
        parcel.writeString(status)
        parcel.writeParcelable(grade, flags)
        parcel.writeParcelable(student, flags)
        parcel.writeParcelable(subject, flags)
        parcel.writeParcelable(teacher, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchRequestData> {
        override fun createFromParcel(parcel: Parcel): BatchRequestData {
            return BatchRequestData(parcel)
        }

        override fun newArray(size: Int): Array<BatchRequestData?> {
            return arrayOfNulls(size)
        }
    }
}