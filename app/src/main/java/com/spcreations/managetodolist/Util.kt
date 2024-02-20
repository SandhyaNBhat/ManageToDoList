package com.spcreations.managetodolist

import android.util.Log
import androidx.annotation.IntRange
import java.text.SimpleDateFormat
import java.util.Date

fun timeConverter(@IntRange(from = 0, to = 23) hour: Int, @IntRange(from = 0, to = 60) minute: Int): String {
    var am = true
    val convertedHour: String = when {
        hour > 12 -> {
            am = false
            "${hour - 12}"
        }
        hour == 12 -> {
            am = false
            hour.toString()
        }
        else -> {
            am = true
            hour.toString()
        }
    }
    val convertedMinute: String = if (minute < 10) "0$minute" else minute.toString()
    val timeOfDay: String = if (am) "am" else "pm"
    return convertedHour.plus(":").plus(convertedMinute).plus(" ").plus(timeOfDay)
}

fun convertLongToDateTime(time: Long):String{
    val date = Date(time)
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return sdf.format(date)
}

fun convertDateToLong(dateTime: Date){
    Log.d("TAG","Coverting Date to Long")
}