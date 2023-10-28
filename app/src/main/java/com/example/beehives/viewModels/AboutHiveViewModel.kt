package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.repositories.MainRepository
import kotlinx.coroutines.launch

class AboutHiveViewModel() : ViewModel(){

    lateinit var repository: MainRepository

    val hive: MutableLiveData<Hive> by lazy { MutableLiveData<Hive>() }

    fun getHive(id: Int) = repository.getHiveByIdLd(id)

    fun updateHive() = viewModelScope.launch { hive.value?.let { repository.updateHive(it) } }

    fun getHiveRevisions(id: Int) = repository.getHiveRevisionsLd(id)

    fun setPhoto(photo: String) = viewModelScope.launch {
        hive.value?.id?.let { repository.setPhotoByHiveId(it, photo) }
    }

    fun deleteCurrentHiveAndRevisions() = viewModelScope.launch {
        hive.value?.id?.let { repository.deleteRevisionsByHiveId(it) }
        hive.value?.id?.let { repository.deleteHiveById(it) }
    }
}