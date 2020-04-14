package com.example.beehives.viewModels

import android.app.Application
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseViewModelTest {

    private lateinit var vm: BaseViewModel
    private val sp: SharedPreferences = mock()
    private val application: Application = mock()
    private val apiary = MutableLiveData<Apiary>(Apiary())
    private val apiaryHives = MutableLiveData<List<Hive>>(listOf(Hive(), Hive()))
    private var repository: MainRepository = mock {
        on { getApiaryByIdLd(any()) } doReturn (apiary)
        on { getApiaryHives(any()) } doReturn (apiaryHives)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        whenever(PreferenceManager.getDefaultSharedPreferences(application)).thenReturn(sp)
    }

    @Test
    fun `init BaseViewModel and observe liveData`(){
        vm = BaseViewModel(application, repository)
        Assert.assertEquals(vm.currentApiary.getOrAwaitValue(), apiary.value)
        Assert.assertEquals(vm.currentApiaryHives.getOrAwaitValue(), apiaryHives.value)

        //DataChange
        apiary.value = Apiary(2,"TEst")
        apiaryHives.value = listOf(Hive(2,3), Hive(3,4))
        Assert.assertEquals(vm.currentApiary.getOrAwaitValue(), apiary.value)
        Assert.assertEquals(vm.currentApiaryHives.getOrAwaitValue(), apiaryHives.value)


        verify(repository, times(1)).getApiaryByIdLd(any())
        verify(repository, times(1)).getApiaryHives(any())

    }
}