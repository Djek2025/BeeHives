package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.beehives.cache.SPCache
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.BeeQueen
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.SEPARATOR
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


open class BaseViewModel() : ViewModel() {

    lateinit var repository: MainRepository
    lateinit var application: Application
    lateinit var settings: SPCache

    val currentApiaryId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val currentApiary: MutableLiveData<Apiary> by lazy { MutableLiveData<Apiary>() }
    val apiaries: MutableLiveData<List<Apiary>> by lazy { MutableLiveData<List<Apiary>>() }
    val hives: MutableLiveData<List<Hive>> by lazy { MutableLiveData<List<Hive>>() }
    val currentHive: MutableLiveData<Hive> by lazy { MutableLiveData<Hive>() }

    init {

    }

    fun getCurrentApiaryId() = currentApiaryId.value

    fun setCurrentApiaryId(id: Int) {
        settings.currentApiaryId = id
        currentApiaryId.value = id
    }

    fun getRepo() = repository

    fun getApiaryById(id: Int) = viewModelScope.async { repository.getApiaryById(id) }

    fun updateApiaryLocation(latitude: String, longitude: String) = viewModelScope.launch {
        repository.updateApiaryLocationById(currentApiaryId.value!!, latitude + SEPARATOR + longitude)
    }

//——————————————————————————————————————————————————————————————————————————————————————————————————

    fun insertApiary(apiary: Apiary) = viewModelScope.launch { repository.insertApiary(apiary) }

    fun updateApiary(apiary: Apiary) = viewModelScope.launch { repository.updateApiary(apiary) }

    fun deleteApiary(apiary: Apiary) = viewModelScope.launch { repository.deleteApiary(apiary) }


    fun insertHive(hive: Hive) = viewModelScope.launch { repository.insertHive(hive) }

    fun updateHive(hive: Hive) = viewModelScope.launch { repository.updateHive(hive) }

    fun deleteHive(hive: Hive) = viewModelScope.launch { repository.deleteHive(hive) }



    fun updateRevision(revision: Revision) = viewModelScope.launch { repository.updateRevision(revision) }

    fun deleteRevision(revision: Revision) = viewModelScope.launch { repository.deleteRevision(revision) }


    fun insertBeeQueen(beeQueen: BeeQueen) = viewModelScope.launch { repository.insertBeeQueen(beeQueen) }
    fun updateBeeQueen(beeQueen: BeeQueen) = viewModelScope.launch { repository.updateBeeQueen(beeQueen) }
}