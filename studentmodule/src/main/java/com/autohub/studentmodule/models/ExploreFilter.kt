package com.autohub.studentmodule.models

/**
 * Created by Vt Netzwelt
 */
data class ExploreFilter(var studentClass: String = "", var subjectName: String = "",
                         var latitude: Float = 0.toFloat(), var longitude: Float = 0.toFloat(), var filterType : String)