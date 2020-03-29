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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.viewModel.BaseViewModel
import kotlinx.android.synthetic.main.fragment_about_apiary.*

class AboutApiaryFragment : Fragment() {

    private lateinit var viewModel : BaseViewModel
    private lateinit var navController : NavController


    companion object {
        @JvmStatic
        fun newInstance() =
            AboutApiaryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance =true
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_apiary, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(apiaryNameEditText)

        viewModel.getCurrentApiary().observe(this@AboutApiaryFragment as LifecycleOwner, Observer {apiary ->

            apiaryNameEditText.text = SpannableStringBuilder(apiary.name)

            when(apiary.location){
                null, "", SEPARATOR -> {
                    locationImageView.setImageResource(R.drawable.baseline_add_location_24)
                    locationImageView.setOnClickListener {
                        navController.navigate(R.id.mapsFragment, bundleOf("request" to "add"))
                    }
                }
                else -> {
                    val latLng = apiary.location!!.split(SEPARATOR)
                    lat.text = getString(R.string.latitude, latLng[0])
                    lng.text = getString(R.string.longitude, latLng[1])
                    locationImageView.setImageResource(R.drawable.baseline_edit_location_24)
                    locationImageView.setOnClickListener {
                        navController.navigate(R.id.mapsFragment, bundleOf("request" to "edit"))
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
