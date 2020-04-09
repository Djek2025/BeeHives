package com.example.beehives.view.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.viewModel.BaseViewModel
import com.example.beehives.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_about_apiary.*

class AboutApiaryFragment : Fragment() {

    private lateinit var viewModel : BaseViewModel
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(BaseViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_apiary, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(apiaryNameEditText)

        viewModel.currentApiary.observe(viewLifecycleOwner, Observer {apiary ->

            apiaryNameEditText.text = SpannableStringBuilder(apiary.name)

            when(apiary.location){
                null, "", SEPARATOR -> {
                    locationImageView.setImageResource(R.drawable.baseline_add_location_24)
                    locationImageView.setOnClickListener {
                        sharedViewModel.mapRequest = "add"
                        navController.navigate(R.id.mapsFragment)
                    }
                }
                else -> {
                    val latLng = apiary.location!!.split(SEPARATOR)
                    lat.text = getString(R.string.latitude, latLng[0])
                    lng.text = getString(R.string.longitude, latLng[1])
                    locationImageView.setImageResource(R.drawable.baseline_edit_location_24)
                    locationImageView.setOnClickListener {
                        sharedViewModel.mapRequest = "edit"
                        navController.navigate(R.id.mapsFragment)
                    }
                }
            }

            buttonSaveName.setOnClickListener {
                if (apiaryNameEditText.text.isNotEmpty()){
                    apiary.name = apiaryNameEditText.text.toString()
                    viewModel.updateApiary(apiary)
                }else Toast.makeText(context, "Error: Name is empty", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
