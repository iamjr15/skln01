package com.autohub.studentmodule.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class BatchesModel(var batchCode: String? = "", var grade: Grade = Grade(), var subject: Subject? = Subject(),
                        var location: Location? = Location(), var id: String? = ""
                        , var name: String? = "", var rate: String? = "", var rating: String? = "",
                        var teacher: Teacher? = Teacher(), var timing: Timing? = Timing(),
                        var title: String? = "", var type: String? = "",
                        var enrolledStudentsId: ArrayList<String>? = ArrayList(),
                        var imagesList: ArrayList<String> = ArrayList(),
                        var batchTiming: String? = "",
                        var status: String? = "",
                        var documentId: String? = "") {


    data class Grade(var id: String? = "", var name: String? = "")
    data class Student(var id: String? = "", var name: String? = "")
    data class Subject(var id: String? = "", var name: String? = "")
    data class Timing(var endTime: Timestamp? = null, var occurances: String? = "", var startTime: Timestamp? = null)
    data class Teacher(var id: String? = "", var name: String? = "", var accountPicture: String? = "",
                       var bioData: String? = "", var instituteName: String? = "")


    data class Location(var geopoint: GeoPoint? = GeoPoint(0.0, 0.0))
}