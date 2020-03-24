package com.example.beehives.view.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.beehives.R
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_about_apiary.*

private const val ARG_CURRENT_APIARY = "Current Apiary"

class AboutApiaryFragment : Fragment() {

    private var currentApiaryId: Int? = null
    private lateinit var viewModel : MainViewModel

    companion object {
        @JvmStatic
        fun newInstance(currentApiaryId: Int, vm : MainViewModel) =
            AboutApiaryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CURRENT_APIARY, currentApiaryId)
                }
                viewModel = vm
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance =true
        arguments?.let {
            currentApiaryId = it.getInt(ARG_CURRENT_APIARY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_apiary, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getApiaryByIdLd(currentApiaryId!!).observe(this@AboutApiaryFragment as LifecycleOwner, Observer {apiary ->

            apiaryNameEditText.text = SpannableStringBuilder(apiary.name)

            when(apiary.location){
                null,"" -> {
                    locationImageView.setImageResource(R.drawable.baseline_add_location_24)
                    locationImageView.setOnClickListener {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container, MapsFragment.newInstance(currentApiaryId!!, viewModel, "add"), "map")
                            .addToBackStack(null)
                            .commit()
                    }
                }
                else -> {
                    val latLng = apiary.location!!.split(SEPARATOR)
                    lat.text = getString(R.string.latitude, latLng[0])
                    lng.text = getString(R.string.longitude, latLng[1])
                    locationImageView.setImageResource(R.drawable.baseline_edit_location_24)
                    locationImageView.setOnClickListener {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container, MapsFragment.newInstance(currentApiaryId!!, viewModel, "edit"), "map")
                            .addToBackStack(null)
                            .commit()
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
