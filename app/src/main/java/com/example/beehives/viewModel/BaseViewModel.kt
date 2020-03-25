package com.example.beehives.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.beehives.model.db.MainDatabase
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.view.activities.SEPARATOR
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepository
    private val database : CollectionReference

    init {
        val dao = MainDatabase.getDatabase(application).hiveDao()
        repository = MainRepository(dao)
        database = FirebaseFirestore.getInstance().collection("Apiaries")
    }

    fun getApiariesLocation () = FirebaseFirestore.getInstance().collection("Apiaries").get()

    fun getApiaryHives(apiaryId : Int) = repository.getApiaryHives(apiaryId)

    fun getApiaryByIdLd(id : Int) = repository.getApiaryByIdLd(id)

    fun getHiveRevisions(hiveId: Int) = repository.getHiveRevisions(hiveId)

    fun getApiaryById(id: Int) = viewModelScope.async { repository.getApiaryById(id) }

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

    fun updateApiary(apiary: Apiary) = viewModelScope.launch {
        repository.updateApiary(apiary)
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

    fun updateApiaryLocationById(id:Int, latitude: String, longitude: String) = viewModelScope.launch {
        repository.updateApiaryLocationById(id, latitude + SEPARATOR + longitude)
    }

    fun insertLocation (latitude:Double, longitude:Double) {
        database.add(mapOf(
            "Coords" to GeoPoint(latitude, longitude),
            "Added" to FieldValue.serverTimestamp()
        ))
    }

    fun updateLocation (latitude:Double, longitude:Double, newlatitude:Double, newlongitude:Double) {
        database.whereEqualTo("Coords", GeoPoint(latitude, longitude)).get().addOnSuccessListener {
            it.forEach {
                it.reference.delete()
            }
            insertLocation(newlatitude, newlongitude)
        }

    }

    fun getAllLocations(gMap: GoogleMap) {
        database.get().addOnSuccessListener {
            it.documents.forEach {
                val temp = it.getGeoPoint("Coords")
                val latlng = LatLng(temp!!.latitude, temp.longitude)
                gMap.addMarker(MarkerOptions().position(latlng).title("Apiary"))
                gMap.addCircle(CircleOptions().center(latlng).radius(1500.00))
            }
        }
    }
}