package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.beehives.model.repositories.MainRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.coroutines.launch

class MapsViewModel(application: Application, repository: MainRepository) : BaseViewModel(application, repository) {

    private val database : CollectionReference = FirebaseFirestore.getInstance().collection("Apiaries")

    fun insertLocation (latitude:Double, longitude:Double) {
        viewModelScope.launch{
            database.add(mapOf(
                "Coords" to GeoPoint(latitude, longitude),
                "Added" to FieldValue.serverTimestamp()
            ))
        }
    }

    fun updateLocation (latitude:Double, longitude:Double, newlatitude:Double, newlongitude:Double) {
        viewModelScope.launch {
            database.whereEqualTo("Coords", GeoPoint(latitude, longitude)).get().addOnSuccessListener {
                it.forEach {
                    it.reference.delete()
                }
                insertLocation(newlatitude, newlongitude)
            }
        }
    }

    fun getAllLocations(): Task<QuerySnapshot> {
        return database.get()
    }
}