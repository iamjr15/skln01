package com.autohub.skln.models.batches

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class BatchesModel(var batchCode: String? = "",
                        var status: String? = "",
                        var grade: Grade? = Grade(),
                        var subject: Subject? = Subject(),
                        var location: Location? = Location(),
                        var id: String? = "",
                        var name: String? = "",
                        var rate: String? = "",
                        var rating: String? = "",
                        var teacher: Teacher? = Teacher(),
                        var timing: Timing? = Timing(),
                        var title: String? = "",
                        var type: String? = "",
                        var enrolledStudentsId: ArrayList<String> = ArrayList(),
                        var imagesList: ArrayList<String> = ArrayList()) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Grade::class.java.classLoader),
            parcel.readParcelable(Subject::class.java.classLoader),
            parcel.readParcelable(Location::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Teacher::class.java.classLoader),
            parcel.readParcelable(Timing::class.java.classLoader),
            parcel.readString(),
            parcel.readString()) {
    }

    data class Grade(var id: String? = "", var name: String? = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Grade> {
            override fun createFromParcel(parcel: Parcel): Grade {
                return Grade(parcel)
            }

            override fun newArray(size: Int): Array<Grade?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Student(var id: String? = "", var name: String? = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Student> {
            override fun createFromParcel(parcel: Parcel): Student {
                return Student(parcel)
            }

            override fun newArray(size: Int): Array<Student?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Subject(var id: String? = "", var name: String? = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Subject> {
            override fun createFromParcel(parcel: Parcel): Subject {
                return Subject(parcel)
            }

            override fun newArray(size: Int): Array<Subject?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Timing(var endTime: Timestamp? = null, var occurances: String? = "", var startTime: Timestamp? = null) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readParcelable(Timestamp::class.java.classLoader),
                parcel.readString(),
                parcel.readParcelable(Timestamp::class.java.classLoader)) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(endTime, flags)
            parcel.writeString(occurances)
            parcel.writeParcelable(startTime, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Timing> {
            override fun createFromParcel(parcel: Parcel): Timing {
                return Timing(parcel)
            }

            override fun newArray(size: Int): Array<Timing?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Teacher(var id: String? = "", var name: String? = "", var accountPicture: String? = "",
                       var bioData: String? = "", var instituteName: String? = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(name)
            parcel.writeString(accountPicture)
            parcel.writeString(bioData)
            parcel.writeString(instituteName)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Teacher> {
            override fun createFromParcel(parcel: Parcel): Teacher {
                return Teacher(parcel)
            }

            override fun newArray(size: Int): Array<Teacher?> {
                return arrayOfNulls(size)
            }
        }
    }


    data class Location(var geopoint: GeoPoint? = GeoPoint(0.0, 0.0)) : Parcelable {
        constructor(parcel: Parcel) : this() {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Location> {
            override fun createFromParcel(parcel: Parcel): Location {
                return Location(parcel)
            }

            override fun newArray(size: Int): Array<Location?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(batchCode)
        parcel.writeString(status)
        parcel.writeParcelable(grade, flags)
        parcel.writeParcelable(subject, flags)
        parcel.writeParcelable(location, flags)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(rate)
        parcel.writeString(rating)
        parcel.writeParcelable(teacher, flags)
        parcel.writeParcelable(timing, flags)
        parcel.writeString(title)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchesModel> {
        override fun createFromParcel(parcel: Parcel): BatchesModel {
            return BatchesModel(parcel)
        }

        override fun newArray(size: Int): Array<BatchesModel?> {
            return arrayOfNulls(size)
        }
    }
}