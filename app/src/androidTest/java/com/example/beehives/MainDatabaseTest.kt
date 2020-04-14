package com.example.beehives

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.beehives.model.db.MainDatabase
import com.example.beehives.model.db.dao.GeneralDao
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.SEPARATOR
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainDatabaseTest {
    private lateinit var database: MainDatabase
    private lateinit var dao: GeneralDao

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            MainDatabase::class.java
        ).build()

        dao = database.generalDao()

        for (i in 1..10) {
            runBlocking { dao.insertRevision(Revision(id = i, hiveId = i)) }
            runBlocking { dao.insertHive(Hive(id = i, apiaryId = i)) }
            runBlocking { dao.insertApiary(Apiary(id = i)) }
        }

    }

    @Test
    fun updateApiaryLocationById() {
        //Given
        var testThanCoordsEmpty = runBlocking { dao.getApiaryById(5) }
        assertEquals(testThanCoordsEmpty.location, SEPARATOR)
        val geoCoords = "123456789${SEPARATOR}123456789"
        //When
        runBlocking { dao.updateApiaryLocationById(5, geoCoords) }
        //Then
        testThanCoordsEmpty = runBlocking { dao.getApiaryById(5) }
        assertEquals(testThanCoordsEmpty.location, geoCoords)

    }

    @Test
    fun getLastRevisionByHiveId() {
        //Given

        //When
        val rev = runBlocking { dao.getLastRevisionByHiveId(5) }
        //Then
        assertEquals(5, rev.id)

    }

    @Test
    fun crudApiary() {

        lateinit var data: Apiary

        //Create - Reed
        runBlocking { dao.insertApiary(Apiary(123)) }
        data = runBlocking { dao.getApiaryById(123) }
        assertEquals(data, Apiary(123))

        //Update - Reed
        runBlocking { dao.updateApiary(data.apply { name = "Test update" }) }
        data = runBlocking { dao.getApiaryById(123) }
        assertEquals(data.name, "Test update")

        //Delete
        runBlocking { dao.deleteApiary(data) }
        data = runBlocking { dao.getApiaryById(123) }

    }

    @Test
    fun crudHive() {

        lateinit var data: Hive

        //Create - Reed
        runBlocking { dao.insertHive(Hive(123)) }
        data = runBlocking { dao.getHiveById(123) }
        assertEquals(data, Hive(123))

        //Update - Reed
        runBlocking { dao.updateHive(data.apply { name = "Test update" }) }
        data = runBlocking { dao.getHiveById(123) }
        assertEquals(data.name, "Test update")

        //Delete
        runBlocking { dao.deleteHive(data) }
        data = runBlocking { dao.getHiveById(123) }
    }

    @Test
    fun crudRevision() {

        lateinit var data: Revision

        //Create - Reed
        runBlocking { dao.insertRevision(Revision(123)) }
        data = runBlocking { dao.getRevisionById(123) }
        assertEquals(data, Revision(123))

        //Update - Reed
        runBlocking { dao.updateRevision(data.apply { date = 154365767680L }) }
        data = runBlocking { dao.getRevisionById(123) }
        assertEquals(data.date, 154365767680L)

        //Delete
        runBlocking { dao.deleteRevision(data) }
        data = runBlocking { dao.getRevisionById(123) }
    }

    @After
    fun after() {
        database.clearAllTables()
        database.close()
    }
}