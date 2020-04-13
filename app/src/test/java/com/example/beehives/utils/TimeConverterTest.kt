package com.example.beehives.utils

import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*

class TimeConverterTest{

    private val timeUnix = 1584999960000L // 2020.03.23 23:46:40
    private val string = "2020.03.23 23:46" //+2 hours because timezone checked
    private val stringShort = "23.03.2020"
    private val tc = TimeConverter()

    private val date: Date = mock()

    @Before
    fun setUp(){
        whenever(date.time).thenReturn( timeUnix )
    }

    @Test
    fun getTimeLong(){
        assertEquals(timeUnix, date.time)
    }

    @Test
    fun longToString() {
        assertEquals(string, tc.longToString(date.time))
    }

    @Test
    fun longToStringShort() {
        assertEquals(stringShort, tc.longToStringShort(date.time))
    }

    @Test
    fun stringToLong() {
        assertEquals(timeUnix, tc.stringToLong(string))
    }
}