package com.example.beehives.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beehives.R
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.viewModel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val ARG_REQUEST = "request"

class MapsFragment : Fragment() {

    private var request : String? = null
    private lateinit var viewModel : MapsViewModel

    companion object {
        @JvmStatic
        fun newInstance() =
            MapsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        arguments?.let {
            request = it.getString( ARG_REQUEST,"show")
        }
    }
//————————————————————————————————————————————————————————————————————————————————————————————————

    private val callback = OnMapReadyCallback { googleMap ->

        val markerCenter = googleMap.addMarker(MarkerOptions()
            .position(googleMap.cameraPosition.target))
        googleMap.setOnCameraMoveListener {
            markerCenter.position = googleMap.cameraPosition.target
        }

        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(49.4285, 32.0620), 6f))

        when (request) {
            "add" -> {
                floatingActionButtonCommit.setOnClickListener {
                    val target = googleMap.cameraPosition.target
                    viewModel.insertLocation(target.latitude, target.longitude)
                    viewModel.updateApiaryLocation(target.latitude.toString(), target.longitude.toString())
                }
            }
            "edit" -> {
                floatingActionButtonCommit.setOnClickListener{
                    val target = googleMap.cameraPosition.target
                    GlobalScope.launch {
                        val oldLoc: List<String> = viewModel.getApiaryById(viewModel.getCurrentApiaryId()).await().location!!.split(SEPARATOR)
                        viewModel.updateLocation(oldLoc[0].toDouble(), oldLoc[1].toDouble(), target.latitude, target.longitude)
                        viewModel.updateApiaryLocation(target.latitude.toString(), target.longitude.toString())
                    }
                }
            }
            else -> {
                floatingActionButtonCommit.visibility = View.GONE
                markerCenter.remove()
                viewModel.getAllLocations(googleMap)
            }
        }
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