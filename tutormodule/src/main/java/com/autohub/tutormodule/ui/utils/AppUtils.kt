package com.autohub.tutormodule.ui.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object AppUtils {

    fun getClassName(name: String): String {
        return when (name) {
            "1" -> "I"
            "2" -> "II"
            "3" -> "III"
            "4" -> "IV"
            "5" -> "V"
            "6" -> "VI"
            "7" -> "VII"
            "8" -> "VIII"
            "9" -> "IX"
            "10" -> "X"
            "11" -> "XI"
            "12" -> "XII"
            else -> "I"
        }
    }


    fun uTCToLocal(dateFormatInPut: String, dateFomratOutPut: String, datesToConvert: String): String? {
        val dateToReturn = datesToConvert

        val sdf = SimpleDateFormat(dateFormatInPut, Locale.US)

        var gmt: Date? = null

        val sdfOutPutToSend = SimpleDateFormat(dateFomratOutPut,Locale.US)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()
        try {
            gmt = sdf.parse(datesToConvert)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
        val targetFormat = SimpleDateFormat("kk:mm")
        val date = originalFormat.parse(gmt.toString())
        val formattedDate = targetFormat.format(date)

        return formattedDate
    }
}