package com.example.beehives.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beehives.model.db.entities.*

@Dao
interface GeneralDao {

    @Query("SELECT * FROM Apiary WHERE id = :id")
    fun getApiaryByIdLd(id: Int): LiveData<Apiary>

    @Query("SELECT * FROM Revision WHERE hiveId = :id AND date = (SELECT max(date) From Revision WHERE hiveId = :id)")
    fun getLastRevisionByHiveId(id: Int): Revision

    @Query("SELECT * FROM Hive WHERE id = :id")
    fun getHiveByIdLd(id: Int): LiveData<Hive>

    @Query("SELECT * FROM Apiary")
    fun getAllApiaries(): LiveData<List<Apiary>>

    @Query("SELECT * FROM Hive WHERE apiaryId = :apiaryId")
    fun getApiaryHives(apiaryId: Int): LiveData<List<Hive>>

    @Query("SELECT * FROM Revision")
    fun getRevisions(): LiveData<List<Revision>>

    @Query("SELECT * FROM BeeQueen WHERE hiveId IS NULL")
    fun getFreeBeeQueen(): LiveData<List<BeeQueen>>

    @Query("SELECT * FROM BeeQueen")
    fun getAllBeeQueen(): LiveData<List<BeeQueen>>

    @Query("SELECT * FROM Apiary WHERE id = :id")
    suspend fun getApiaryById(id: Int): Apiary

    @Query("SELECT * FROM Hive WHERE id = :id")
    suspend fun getHiveById(id: Int): Hive

    @Query("SELECT * FROM Revision WHERE id = :id")
    suspend fun getRevisionById(id: Int): Revision

    @Query("SELECT * FROM Revision WHERE hiveId = :hiveId")
    fun getHiveRevisionsLd(hiveId: Int): LiveData<List<Revision>>

    @Query("SELECT * FROM Revision WHERE hiveId = :hiveId")
    fun getHiveRevisions(hiveId: Int): List<Revision>

    @Query("SELECT id FROM Hive WHERE label = :label")
    suspend fun getHiveIdByLabel(label: String): Int

    @Query("UPDATE Hive SET label = :label WHERE id = :id")
    suspend fun setLabelByHiveId(id: Int, label: String)

    @Query("UPDATE Hive SET photo = :photo WHERE id = :id")
    suspend fun setPhotoByHiveId(id: Int, photo: String)

    @Query("UPDATE Apiary SET location = :location WHERE id = :id")
    suspend fun updateApiaryLocationById(id:Int, location : String)

    @Query("DELETE FROM Hive WHERE id = :hiveId")
    suspend fun deleteHiveById(hiveId: Int)

    @Query("DELETE FROM Revision WHERE hiveId = :hiveId")
    suspend fun deleteRevisionsByHiveId(hiveId: Int)

    //———————————————————————————————————————————

    @Insert
    suspend fun insertHive(item: Hive)

    @Insert
    suspend fun insertApiary(item: Apiary)

    @Insert
    suspend fun insertRevision(item: Revision)

    @Insert
    suspend fun insertBeeQueen(item: BeeQueen)

    //———————————————————————————————————————————
    @Update
    suspend fun updateHive(item: Hive)

    @Update
    suspend fun updateApiary(item: Apiary)

    @Update
    suspend fun updateRevision(item: Revision)

    @Update
    suspend fun updateBeeQueen(beeQueen: BeeQueen)

    //———————————————————————————————————————————

    @Delete
    suspend fun deleteHive(item: Hive)

    @Delete
    suspend fun deleteApiary(item: Apiary)

    @Delete
    suspend fun deleteRevision(item: Revision)

    @Delete
    suspend fun deleteBeeQueen(item: BeeQueen)


}