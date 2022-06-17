package com.example.seniorbetterlife.utils

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {
    companion object{
        fun getDate(): String {
            val sdf = SimpleDateFormat("dd/M/yyyy")
            return sdf.format(Date())
        }
        fun getDateWithTime(): String {
            val sdf = SimpleDateFormat("yyyy.MM.dd 'godz' HH:mm:ss")
            return sdf.format(Date())
        }

        fun getTimeInMillis(timeToParse: String): Long{
            val cal = Calendar.getInstance()
            if(timeToParse.length == 5)
                cal.set(Calendar.HOUR_OF_DAY,timeToParse.take(2).toInt())
            else
                cal.set(Calendar.HOUR_OF_DAY,timeToParse.take(1).toInt())
            cal.set(Calendar.MINUTE,timeToParse.takeLast(2).toInt())
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }
    }

}