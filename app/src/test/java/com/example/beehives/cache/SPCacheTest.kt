package com.example.beehives.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.nhaarman.mockitokotlin2.doReturnConsecutively
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SPCacheTest{

    private val context: Context = mock()
    private val sp : SharedPreferences = mock()
    private var spc: SPCache = mock{
        on{isFirstLaunch}doReturnConsecutively (listOf(true, false))
        on{nightMode}doReturnConsecutively (listOf(true, false))
        on{units}doReturnConsecutively (listOf("metric", "imperial"))
        on{language}doReturnConsecutively (listOf("en", "uk"))
        on{currentApiaryId}doReturnConsecutively (listOf(1, 2))
    }


    @Before
    fun setUp(){
        spc = SPCache(sp)
    }

    @Test
    fun `test get's and set's methods`(){

    }

}