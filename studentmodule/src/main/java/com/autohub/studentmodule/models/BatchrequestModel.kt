package com.autohub.studentmodule.models

import android.os.Parcel
import android.os.Parcelable

data class BatchrequestModel(var id: String? = "", var status: String? = "", var grade: Grade? = Grade()
                             , var student: Student? = Student(),
                             var teacher: Teacher? = Teacher(), var subject: Subject? = Subject()) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Grade::class.java.classLoader),
            parcel.readParcelable(Student::class.java.classLoader),
            parcel.readParcelable(Teacher::class.java.classLoader),
            parcel.readParcelable(Subject::class.java.classLoader))


    data class Grade(var id: String? = "", var name: String? = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString())


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
                parcel.readString())

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

    data class Teacher(var id: String? = "", var name: String? = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString())


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(name)
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

    data class Subject(var id: String? = "", var name: String? = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString())


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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(status)
        parcel.writeParcelable(grade, flags)
        parcel.writeParcelable(student, flags)
        parcel.writeParcelable(teacher, flags)
        parcel.writeParcelable(subject, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchrequestModel> {
        override fun createFromParcel(parcel: Parcel): BatchrequestModel {
            return BatchrequestModel(parcel)
        }

        override fun newArray(size: Int): Array<BatchrequestModel?> {
            return arrayOfNulls(size)
        }
    }

}



