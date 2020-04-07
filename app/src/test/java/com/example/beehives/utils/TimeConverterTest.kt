package com.example.beehives.utils

import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.stubbing.Answer
import java.text.SimpleDateFormat
import java.util.*

class TimeConverterTest{

    private val timeUnix = 1584999960000L // 2020.03.23 23:46:40
    private val string = "2020.03.23 23:46" //+2 hours because timezone checked
    private val stringShort = "23.03.2020"

    private val date: Date = mock()

    @Before
    fun setUp(){
        whenever(date.time).thenAnswer(Answer { timeUnix })
    }

    @Test
    fun getTimeLong(){
        assertEquals(timeUnix, date.time)
    }

    @Test
    fun longToString() {
        val date = Date(timeUnix)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        assertEquals(string, format.format(date))
    }

    @Test
    fun longToStringShort() {
        val date = Date(timeUnix)
        val format = SimpleDateFormat("dd.MM.yyyy")
        assertEquals(stringShort, format.format(date))
    }

    @Test
    fun stringToLong() {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        assertEquals(timeUnix, df.parse(string).time)
    }
}