package com.example.beehives.viewModels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AboutHiveViewModelTest{

    private lateinit var vm: AboutHiveViewModel
    private val application: Application = mock()
    private val testData = MutableLiveData<Hive>(Hive())
    private val testDataList = MutableLiveData<List<Revision>>(listOf(Revision()))
    private var repository: MainRepository = mock {
        on { getHiveByIdLd(any()) } doReturn (testData)
        on { getHiveRevisions(any()) } doReturn (testDataList)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        vm = AboutHiveViewModel(application, repository)
    }

    @Test
    fun `test mutable live data`(){

        vm.getHive(any()).observeForever {
            vm.hive.value = testData.value
        }
        assertEquals(vm.hive.getOrAwaitValue(), testData.value)

        //DataChange
        testData.value = Hive(1, 1, "Test")
        assertEquals(vm.hive.getOrAwaitValue(), testData.value)

    }

    @Test
    fun `test mutable list live data`(){
        lateinit var buffer: List<Revision>
        vm.getHiveRevisions(any()).observeForever {
            buffer = it
        }
        assertEquals(buffer, testDataList.value)

        //Data change
        testDataList.value = listOf(Revision(2,3), Revision(3,4))
        assertEquals(buffer, testDataList.value)

    }

}