package com.autohub.studentmodule.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
        return targetFormat.format(date)
    }


    fun decodeSampledBitmapFromResource(filePath: String,
                                        reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}