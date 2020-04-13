package com.example.beehives.viewModels

import android.app.Application
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.repositories.MainRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HivesViewModelTest{
    private lateinit var vm: HivesViewModel
    private val application: Application = mock()
    private val sp: SharedPreferences = mock()
    private var repository: MainRepository = mock {
        on { getApiaryByIdLd(any()) } doReturn (MutableLiveData<Apiary>(Apiary()))
        on { getApiaryHives(any()) } doReturn (MutableLiveData<List<Hive>>(listOf(Hive())))
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        whenever(PreferenceManager.getDefaultSharedPreferences(application)).thenReturn(sp)
        vm = HivesViewModel(application, repository)
    }

    @Test
    fun `checked must be false after init`(){
        assertEquals(vm.checked.value, false)
    }

    @Test
    fun `checked hive list == null`(){
        assertNull(vm.checkedHives.value)
    }
}