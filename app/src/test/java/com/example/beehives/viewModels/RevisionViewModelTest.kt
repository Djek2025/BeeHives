package com.example.beehives.viewModels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RevisionViewModelTest {
    private lateinit var vm: RevisionViewModel
    private val application: Application = mock()
    private var repository: MainRepository = mock {
        on { runBlocking { getLastRevisionByHiveId(any()) } } doReturn (Revision(2, 5))
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        vm = RevisionViewModel(application, repository)
    }

    @Test
    fun `get last revision`() {
        runBlocking {
            assertNull(vm.lastRevision.value)
            vm.lastRevision.value = vm.getLastRev(any()).await()
            assertNotNull(vm.lastRevision.getOrAwaitValue())
        }
    }
}