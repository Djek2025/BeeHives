package com.example.beehives.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beehives.model.db.entities.*

@Dao
interface GeneralDao {

    @Query("SELECT * FROM Apiary WHERE id = :id")
    fun getApiaryByIdLd(id: Int): LiveData<Apiary>

    @Query("SELECT * FROM Hive WHERE id = :id")
    fun getHiveByIdLd(id: Int): LiveData<Hive>

    @Query("SELECT * FROM Apiary")
    fun getAllApiaries(): LiveData<List<Apiary>>

    @Query("SELECT * FROM Hive WHERE apiaryId = :apiaryId")
    fun getApiaryHives(apiaryId: Int): LiveData<List<Hive>>

    @Query("SELECT * FROM Apiary WHERE id = :id")
    suspend fun getApiaryById(id: Int): Apiary

    @Query("SELECT * FROM Hive WHERE id = :id")
    suspend fun getHiveById(id: Int): Hive

    @Query("SELECT * FROM Revision WHERE hiveId = :hiveId")
    fun getHiveRevisions(hiveId: Int): LiveData<List<Revision>>

    //———————————————————————————————————————————

    @Insert
    suspend fun insertHive(item: Hive)

    @Insert
    suspend fun insertApiary(item: Apiary)

    @Insert
    suspend fun insertRevision(item: Revision)

    //———————————————————————————————————————————
    @Update
    suspend fun updateHive(item: Hive)

    @Update
    suspend fun updateApiary(item: Apiary)

    @Update
    suspend fun updateRevision(item: Revision)

    @Query("UPDATE Apiary SET location = :location WHERE id = :id")
    suspend fun updateApiaryLocationById(id:Int, location : String)
    //———————————————————————————————————————————

    @Delete
    suspend fun deleteHive(item: Hive)

    @Delete
    suspend fun deleteApiary(item: Apiary)

    @Delete
    suspend fun deleteRevision(item: Revision)

}