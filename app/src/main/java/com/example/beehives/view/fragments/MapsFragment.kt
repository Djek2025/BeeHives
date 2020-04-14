package com.example.beehives.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.beehives.R
import com.example.beehives.databinding.FragmentMapsBinding
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.SEPARATOR
import com.example.beehives.viewModels.MapsViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.utils.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {

    private lateinit var viewModel : MapsViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var binding: FragmentMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideViewModelFactory(activity!!.application)
        viewModel = ViewModelProvider(this, factory).get(MapsViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SharedViewModel::class.java)
    }
//————————————————————————————————————————————————————————————————————————————————————————————————

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(49.4285, 32.0620), 6f))

        val icon = BitmapDescriptorFactory.fromResource(R.drawable.beehive)

        val markerCenter = googleMap.addMarker(MarkerOptions()
            .position(googleMap.cameraPosition.target).icon(icon))

        googleMap.setOnCameraMoveListener { markerCenter.position = googleMap.cameraPosition.target }

        when (sharedViewModel.mapRequest) {
            "add" -> {
                floatingActionButtonCommit.setOnClickListener {
                    val target = googleMap.cameraPosition.target
                    viewModel.insertLocation(target.latitude, target.longitude)
                    viewModel.updateApiaryLocation(target.latitude.toString(), target.longitude.toString())
                    activity?.onBackPressed()
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
                    Toast.makeText(context,"OK", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                markerCenter.remove()
                viewModel.getAllLocations().addOnSuccessListener { it ->
                    it.documents.forEach {
                        val temp = it.getGeoPoint("Coords")
                        val latlng = LatLng(temp!!.latitude, temp.longitude)
                        googleMap.addMarker(MarkerOptions().position(latlng).title("Apiary").icon(icon))
                        googleMap.addCircle(
                            CircleOptions().center(latlng)
                                .radius(1500.00)
                                .strokeWidth(0f)
                                .fillColor(getThemeColor())
                        )
                    }
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = sharedViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.mapRequest = "show"
    }

    @ColorInt
    fun getThemeColor(): Int = 0x80FFFF00.toInt()
}