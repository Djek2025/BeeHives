package com.example.beehives.model.repositories

import com.example.beehives.model.db.dao.GeneralDao
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision

class MainRepository(private val dao: GeneralDao) {

    fun getApiaryHives(apiaryId: Int) = dao.getApiaryHives(apiaryId)

    fun getApiaryByIdLd(id: Int) = dao.getApiaryByIdLd(id)

    fun getHiveByIdLd(id: Int) = dao.getHiveByIdLd(id)

    fun getHiveRevisions(hiveId: Int) = dao.getHiveRevisions(hiveId)

    suspend fun getApiaryById(id: Int) =  dao.getApiaryById(id)

    suspend fun getHiveById(id: Int) = dao.getHiveById(id)

    suspend fun getHiveIdByLabel(label: String) = dao.getHiveIdByLabel(label)

    suspend fun setLabelByHiveId(id: Int, label: String) = dao.setLabelByHiveId(id, label)

    suspend fun updateApiaryLocationById(id:Int, location : String) {
        dao.updateApiaryLocationById(id, location)
    }

//——————————————————————————————————————————————————————————————————————————————————————————————————
    suspend fun insertApiary(apiary: Apiary) {
        dao.insertApiary(apiary)
    }

    suspend fun updateApiary(apiary: Apiary) {
        dao.updateApiary(apiary)
    }

    suspend fun deleteApiary(apiary: Apiary){
        dao.deleteApiary(apiary)
    }

    suspend fun insertHive(hive: Hive) {
        dao.insertHive(hive)
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