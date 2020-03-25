package com.example.beehives.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeConverter {

    fun getTimeLong(): Long{
        return System.currentTimeMillis()
    }

    fun longToString(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

    fun stringToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.parse(date).time
    }
}