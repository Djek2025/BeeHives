package com.example.beehives.model.repositories

import com.example.beehives.model.db.dao.GeneralDao
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision

class MainRepository(private val dao: GeneralDao) {

    fun getApiaryHives(apiaryId: Int) = dao.getApiaryHives(apiaryId)

    fun getApiaryById(id: Int) = dao.getApiaryById(id)
    suspend fun getHiveById(id: Int) = dao.getHiveById(id)
    fun getHiveRevisions(hiveId: Int) = dao.getHiveRevisions(hiveId)

    suspend fun insertHive(hive: Hive) {
        dao.insertHive(hive)
    }

    suspend fun insertApiary(apiary: Apiary) {
        dao.insertApiary(apiary)
    }

    suspend fun updateHive(hive: Hive) {
        dao.updateHive(hive)
    }

    suspend fun deleteHive(hive: Hive) {
        dao.deleteHive(hive)
    }

    suspend fun insertRevision(revision: Revision) {
        dao.insertRevision(revision)
    }

    suspend fun updateRevision(revision: Revision) {
        dao.updateRevision(revision)
    }

    suspend fun deleteRevision(revision: Revision) {
        dao.updateRevision(revision)
    }
}