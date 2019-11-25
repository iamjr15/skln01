package com.autohub.studentmodule.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AppUtil {
    fun uTCToLocal(dateFormatInPut: String, dateFomratOutPut: String, datesToConvert: String): String? {
        val sdf = SimpleDateFormat(dateFormatInPut)

        var gmt: Date? = null

        val sdfOutPutToSend = SimpleDateFormat(dateFomratOutPut)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()
        try {
            gmt = sdf.parse(datesToConvert)

        } catch (e: ParseException) {
            e.printStackTrace()
        }


        val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
        val targetFormat = SimpleDateFormat("h:mm a")
        val date = originalFormat.parse(gmt.toString())
        val formattedDate = targetFormat.format(date)
        return formattedDate
    }

}