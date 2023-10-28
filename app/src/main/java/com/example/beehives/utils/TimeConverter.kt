package com.example.beehives.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeConverter {

    fun getTimeLong(): Long{
        return System.currentTimeMillis()
    }

    fun longToString(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        return format.format(date)
    }

    fun longToStringShort(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    fun longToStringShortShort(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM")
        return format.format(date)
    }

    fun stringToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        return df.parse(date).time
    }

    fun longToShortLong(time: Long): Long{
        val str = longToString(time)
        return  stringToLong(str)
    }

}