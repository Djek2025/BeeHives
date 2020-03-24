package com.example.beehives.view.fragments

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.beehives.R
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.viewModel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val ARG_CURRENT_APIARY = "current_apiary"
private const val ARG_INTENT = "argument_for_map"

class MapsFragment : Fragment() {

    private var currentApiaryId : Int? = null
    private var intent : String? = null
    private lateinit var viewModel : MainViewModel

    companion object {
        @JvmStatic
        fun newInstance(currentApiaryId: Int, vm : MainViewModel, intent : String) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CURRENT_APIARY, currentApiaryId)
                    putString(ARG_INTENT,intent)
                }
                viewModel = vm
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentApiaryId = it.getInt(ARG_CURRENT_APIARY)
            intent = it.getString(ARG_INTENT)
        }
        retainInstance = true
    }
//————————————————————————————————————————————————————————————————————————————————————————————————

    private val callback = OnMapReadyCallback { googleMap ->

        val markerCenter = googleMap.addMarker(MarkerOptions().position(googleMap.cameraPosition.target))
        googleMap.setOnCameraMoveListener {
            markerCenter.position = googleMap.cameraPosition.target //to center in map
        }

        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(49.4285, 32.0620), 6f))

        when (intent) {
            "add" -> {
                floatingActionButtonCommit.setOnClickListener {
                    val target = googleMap.cameraPosition.target
                    viewModel.insertLocation(target.latitude, target.longitude)
                    viewModel.updateApiaryLocationById(
                        currentApiaryId!!,
                        target.latitude.toString(),
                        target.longitude.toString()
                    )
                }
            }
            "edit" -> {
                floatingActionButtonCommit.setOnClickListener{
                    val target = googleMap.cameraPosition.target
                    GlobalScope.launch {
                        val oldLoc: List<String> = viewModel.getApiaryById(currentApiaryId!!).await().location!!.split(SEPARATOR)
                        viewModel.updateLocation(oldLoc[0].toDouble(), oldLoc[1].toDouble(),
                            target.latitude, target.longitude)
                        viewModel.updateApiaryLocationById(currentApiaryId!!,
                            target.latitude.toString(), target.longitude.toString())
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