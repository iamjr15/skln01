package com.autohub.tutormodule.ui.utils

import com.autohub.tutormodule.ui.utils.AppConstants.DateFormatInput
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


    fun uTCToLocal(dateFormatInput: String, dateFormatOutput: String, datesToConvert: String): String? {
        val dateToReturn = datesToConvert

        val sdf = SimpleDateFormat(dateFormatInput, Locale.US)

        var gmt: Date? = null

        val sdfOutPutToSend = SimpleDateFormat(dateFormatOutput,Locale.US)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()
        try {
            gmt = sdf.parse(datesToConvert)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val originalFormat = SimpleDateFormat(DateFormatInput, Locale.US)
        val targetFormat = SimpleDateFormat("kk:mm")
        val date = originalFormat.parse(gmt.toString())

        return targetFormat.format(date)
    }
}