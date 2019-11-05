package com.autohub.skln.models

data class ExploreFilter(var studentClass: String = "", var subjectName: String = "",
                         var latitude: Float = 0.toFloat(), var longitude: Float = 0.toFloat(), var filterType : String) {
}