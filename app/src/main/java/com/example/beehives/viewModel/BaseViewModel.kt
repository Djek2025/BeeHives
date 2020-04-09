package com.example.beehives.viewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.beehives.model.db.MainDatabase
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.view.activities.SEPARATOR
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val ARG_FIRST_RUN = "first_run"
private const val ARG_CURRENT_APIARY = "current_apiary"

open class BaseViewModel(application: Application) : AndroidViewModel(application){

    private val repository: MainRepository
    private val settings : SharedPreferences
    private var currentApiaryId: Int
    val currentApiary: MutableLiveData<Apiary> by lazy { MutableLiveData<Apiary>() }
    val currentApiaryHives: MutableLiveData<List<Hive>> by lazy { MutableLiveData<List<Hive>>() }

    init {
        val dao = MainDatabase.getDatabase(application).generalDao()
        repository = MainRepository(dao)
        settings = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)

        currentApiaryId = if (settings.getBoolean(ARG_FIRST_RUN, true)) {
            settings.edit().putBoolean(ARG_FIRST_RUN, false).apply()
            insertApiary(Apiary(id = 1))
            1
        } else {
            settings.getInt(ARG_CURRENT_APIARY, 1)
        }

        repository.getApiaryByIdLd(currentApiaryId).observeForever {
            currentApiary.value = it
        }
        repository.getApiaryHives(currentApiaryId).observeForever {
            currentApiaryHives.value = it
        }
    }

    fun getRepo() = repository

    fun getSettings() = settings

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


    fun insertHive(hive: Hive) = viewModelScope.launch { repository.insertHive(hive) }

    fun updateHive(hive: Hive) = viewModelScope.launch { repository.updateHive(hive) }

    fun deleteHive(hive: Hive) = viewModelScope.launch { repository.deleteHive(hive) }


    fun insertRevison(revision: Revision) = viewModelScope.launch { repository.insertRevision(revision) }

    fun updateRevison(revision: Revision) = viewModelScope.launch { repository.updateRevision(revision) }

    fun deleteRevison(revision: Revision) = viewModelScope.launch { repository.deleteRevision(revision) }
}