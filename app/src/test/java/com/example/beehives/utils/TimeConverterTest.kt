package com.example.beehives.utils

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class TimeConverterTest{
    lateinit var tc : TimeConverter
    val long = 1585500000L
    val res = "29.03.2020 16:40"
    val resShort = "29.03.2020"

    @Before
    fun setUp(){
        tc = TimeConverter()
    }

    @Test
    fun getTimeLong() {
        assertTrue(tc.getTimeLong() != null)
        assertTrue(tc.getTimeLong() is Long)
    }

    @Test
    fun longToString() {
        assertEquals(tc.longToString(long), res)
    }

    @Test
    fun longToStringShort() {
        assertEquals(tc.longToString(long), resShort)
    }

    @Test
    fun stringToLong() {
        assertEquals(long, tc.stringToLong(res))
    }
}