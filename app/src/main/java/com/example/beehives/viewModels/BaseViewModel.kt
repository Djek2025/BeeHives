package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.beehives.cache.SPCache
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.SEPARATOR
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


open class BaseViewModel(application: Application, private val repository: MainRepository) : AndroidViewModel(application) {

    private var currentApiaryId: Int = 1
    val currentApiary: MutableLiveData<Apiary> by lazy { MutableLiveData<Apiary>() }
    val currentApiaryHives: MutableLiveData<List<Hive>> by lazy { MutableLiveData<List<Hive>>() }

    init {
        currentApiaryId = SPCache(PreferenceManager.getDefaultSharedPreferences(application)).currentApiaryId

        repository.getApiaryByIdLd(currentApiaryId).observeForever {
            currentApiary.value = it
        }
        repository.getApiaryHives(currentApiaryId).observeForever {
            currentApiaryHives.value = it
        }
    }

    fun getCurrentApiaryId() = currentApiaryId

    fun setCurrentApiaryId(id: Int) {currentApiaryId = id}

    fun getApiaryById(id: Int) = viewModelScope.async { repository.getApiaryById(id) }

    fun updateApiaryLocation(latitude: String, longitude: String) = viewModelScope.launch {
        repository.updateApiaryLocationById(currentApiaryId, latitude + SEPARATOR + longitude)
    }

//——————————————————————————————————————————————————————————————————————————————————————————————————

    fun insertApiary(apiary: Apiary) = viewModelScope.launch { repository.insertApiary(apiary) }

    fun updateApiary(apiary: Apiary) = viewModelScope.launch { repository.updateApiary(apiary) }

    fun deleteApiary(apiary: Apiary) = viewModelScope.launch { repository.deleteApiary(apiary) }



    fun updateHive(hive: Hive) = viewModelScope.launch { repository.updateHive(hive) }

    fun deleteHive(hive: Hive) = viewModelScope.launch { repository.deleteHive(hive) }



    fun updateRevision(revision: Revision) = viewModelScope.launch { repository.updateRevision(revision) }

    fun deleteRevision(revision: Revision) = viewModelScope.launch { repository.deleteRevision(revision) }
}