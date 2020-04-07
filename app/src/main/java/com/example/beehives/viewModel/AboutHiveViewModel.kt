package com.example.beehives.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.repositories.MainRepository
import kotlinx.coroutines.launch

class AboutHiveViewModel(application: Application) : BaseViewModel(application){

    private var repository: MainRepository = getRepo()
    val hive: MutableLiveData<Hive> by lazy { MutableLiveData<Hive>() }

    fun getHive(id: Int) = repository.getHiveByIdLd(id)
    fun getHiveRevisions(id: Int) = repository.getHiveRevisions(id)

    fun setPhoto(photo: String) = viewModelScope.launch {
        hive.value?.id?.let { repository.setPhotoByHiveId(it, photo) }
    }

    fun deleteCurrentHiveAndRevisions() = viewModelScope.launch {
        hive.value?.id?.let { repository.deleteRevisionsByHiveId(it) }
        hive.value?.id?.let { repository.deleteHiveById(it) }
    }
}