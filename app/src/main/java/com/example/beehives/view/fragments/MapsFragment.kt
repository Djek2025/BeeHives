package com.example.beehives.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.beehives.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore


class MapsFragment : Fragment() {

    fun getApiaries(gMap: GoogleMap) {
        val database = FirebaseFirestore.getInstance()
        database.collection("Apiaries").get().addOnSuccessListener {
            it.documents.forEach {
                val temp = it.getGeoPoint("Coords")
                val latlng = LatLng(temp!!.latitude, temp.longitude)
                gMap.addMarker(MarkerOptions().position(latlng).title("Apiary"))
                gMap.addCircle(CircleOptions().center(latlng).radius(3000.00))
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val cherkasy = LatLng(49.4285, 32.0620)
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cherkasy, 6f))
        getApiaries(googleMap)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}