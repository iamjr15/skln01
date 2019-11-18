package com.autohub.tutormodule.ui.utils

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
}