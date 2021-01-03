package com.faz.cleannews.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun getDate(dateString: String?): String? {
    return try {
        val format1 = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        dateString?.let {
            val date = format1.parse(dateString)
            val sdf: DateFormat = SimpleDateFormat("MMM d, yyyy")
            date?.let {
                sdf.format(date)
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        "xx"
    }
}

@SuppressLint("SimpleDateFormat")
fun getTime(dateString: String?): String? {
    return try {
        val format1 = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        dateString?.let {
            val date = format1.parse(dateString)
            val sdf: DateFormat = SimpleDateFormat("h:mm a")
            date?.let {
                sdf.format(date)
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        "xx"
    }
}

fun getRandomNumber(): Long {
    return (Math.random() * (100000 + 1) + 0).toLong()
}
