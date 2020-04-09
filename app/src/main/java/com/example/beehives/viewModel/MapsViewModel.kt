package com.example.beehives.viewModel

import android.app.Application
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.lifecycle.viewModelScope
import com.example.beehives.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.coroutines.launch


class MapsViewModel(application: Application) : BaseViewModel(application) {

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