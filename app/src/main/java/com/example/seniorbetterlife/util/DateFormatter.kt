package com.example.seniorbetterlife.util

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
    }

}