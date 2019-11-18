package com.autohub.studentmodule.models

/**
 * Created by Vt Netzwelt
 */
data class ExploreFilter(var studentClass: String = "", var subjectName: String = "",
                         var latitude: Double = 0.0, var longitude: Double = 0.0, var filterType: String)