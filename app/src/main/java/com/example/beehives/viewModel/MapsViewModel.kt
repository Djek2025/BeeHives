package com.example.beehives.viewModel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch

class MapsViewModel(application: Application) : BaseViewModel(application) {
    private val database : CollectionReference

    init {
        database = FirebaseFirestore.getInstance().collection("Apiaries")
    }



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