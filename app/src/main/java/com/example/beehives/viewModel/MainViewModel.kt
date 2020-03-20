package com.example.beehives.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.beehives.model.db.MainDatabase
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepository

    init {
        val dao = MainDatabase.getDatabase(application).hiveDao()
        repository = MainRepository(dao)
    }

    fun getApiaryHives(apiaryId : Int) = repository.getApiaryHives(apiaryId)

    fun getHiveRevisions(hiveId: Int) = repository.getHiveRevisions(hiveId)

    fun getHiveById(id: Int) = viewModelScope.async { repository.getHiveById(id) }

    fun insertHive(hive: Hive) = viewModelScope.launch {
        repository.insertHive(hive)
    }

    fun updateHive(hive: Hive) = viewModelScope.launch {
        repository.updateHive(hive)
    }

    fun deleteHive(hive: Hive) = viewModelScope.launch {
        repository.deleteHive(hive)
    }

    fun insertApiary(apiary: Apiary) = viewModelScope.launch {
        repository.insertApiary(apiary)
    }

    fun insertRevison(revision: Revision) = viewModelScope.launch {
        repository.insertRevision(revision)
    }

    fun updateRevison(revision: Revision) = viewModelScope.launch {
        repository.updateRevision(revision)
    }

    fun deleteRevison(revision: Revision) = viewModelScope.launch {
        repository.deleteRevision(revision)
    }
}