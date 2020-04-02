package com.example.beehives.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.example.beehives.model.db.MainDatabase
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.view.fragments.SettingsFragment
import com.google.firebase.firestore.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val ARG_FIRST_RUN = "first_run"
private const val ARG_CURRENT_APIARY = "current_apiary"

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepository
    private val settings : SharedPreferences
    private var currentApiaryId: Int
    private var currentApiary: LiveData<Apiary>
    private var currentApiaryHives: LiveData<List<Hive>>
//    private var mapState: MutableLiveData<String>

    init {
        val dao = MainDatabase.getDatabase(application).hiveDao()
        repository = MainRepository(dao)
        settings = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)

        currentApiaryId = if (settings.getBoolean(ARG_FIRST_RUN, true)) {
            settings.edit().putBoolean(ARG_FIRST_RUN, false).apply()
            insertApiary(Apiary())
            1
        } else {
            settings.getInt(ARG_CURRENT_APIARY, 1)
        }

        currentApiary = getApiaryByIdLd(currentApiaryId)
        currentApiaryHives = getApiaryHives(currentApiaryId)
    }

    fun getCurrentApiaryId() = currentApiaryId
    fun getCurrentApiary() = currentApiary
    fun getCurrentApiaryHives() = currentApiaryHives

    fun getApiariesLocation () = FirebaseFirestore.getInstance().collection("Apiaries").get()

    fun getApiaryHives(apiaryId : Int) = repository.getApiaryHives(apiaryId)

    fun getApiaryByIdLd(id : Int) = repository.getApiaryByIdLd(id)

    fun getHiveByIdLd(id : Int) = repository.getHiveByIdLd(id)

    fun getHiveRevisions(hiveId: Int) = repository.getHiveRevisions(hiveId)

    fun getApiaryById(id: Int) = viewModelScope.async { repository.getApiaryById(id) }

    fun getHiveById(id: Int) = viewModelScope.async { repository.getHiveById(id) }

    fun updateApiaryLocation(latitude: String, longitude: String) = viewModelScope.launch {
        repository.updateApiaryLocationById(currentApiaryId, latitude + SEPARATOR + longitude)
    }

    fun getHiveIdByLabel(label: String) = viewModelScope.async{ repository.getHiveIdByLabel(label) }

    fun setLabelByHiveId(id: Int, label: String) = viewModelScope.launch { repository.setLabelByHiveId(id, label)}
    fun setPhotoByHiveId(id: Int, photo: String) = viewModelScope.launch { repository.setPhotoByHiveId(id, photo)}

    fun deleteHiveAndRevisions(hiveId: Int) = viewModelScope.launch {
        repository.deleteHiveById(hiveId)
        repository.deleteRevisionsByHiveId(hiveId)
    }

//——————————————————————————————————————————————————————————————————————————————————————————————————
    fun insertHive(hive: Hive) = viewModelScope.launch {
        repository.insertHive(hive)
    }

    fun updateHive(hive: Hive) = viewModelScope.launch {
        repository.updateHive(hive)
    }

    fun deleteHive(hive: Hive) = viewModelScope.launch {
        repository.deleteHive(hive)
    }

    fun updateApiary(apiary: Apiary) = viewModelScope.launch {
        repository.updateApiary(apiary)
    }

    fun insertApiary(apiary: Apiary) = viewModelScope.launch {
        repository.insertApiary(apiary)
    }

    fun deleteApiary(apiary: Apiary) = viewModelScope.launch { repository.deleteApiary(apiary) }

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