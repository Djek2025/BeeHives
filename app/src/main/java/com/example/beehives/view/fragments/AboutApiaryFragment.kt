package com.example.beehives.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.databinding.FragmentAboutApiaryBinding
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.SEPARATOR
import com.example.beehives.viewModels.BaseViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_about_apiary.*

class AboutApiaryFragment : Fragment() {

    private lateinit var viewModel : BaseViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var navController : NavController
    private lateinit var binding: FragmentAboutApiaryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideViewModelFactory(activity!!.application)
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner, factory).get(BaseViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_apiary, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(apiaryNameEditText)

        viewModel.currentApiary.observe(viewLifecycleOwner, Observer {apiary ->

            val latLng = apiary.location!!.split(SEPARATOR)
            lat.text = getString(R.string.latitude, latLng[0])
            lng.text = getString(R.string.longitude, latLng[1])
//            apiaryNameEditText.text = SpannableStringBuilder(apiary.name)

            when(apiary.location){
                null, "", SEPARATOR -> {
                    locationImageView.setOnClickListener {
                        sharedViewModel.mapRequest = "add"
                        navController.navigate(R.id.action_aboutApiaryFragment_to_mapsFragment)
                    }
                }
                else -> {
                    locationImageView.setOnClickListener {
                        sharedViewModel.mapRequest = "edit"
                        navController.navigate(R.id.action_aboutApiaryFragment_to_mapsFragment)
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
