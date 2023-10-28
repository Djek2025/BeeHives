package com.example.beehives.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.databinding.FragmentAboutApiaryBinding
import com.example.beehives.utils.*
import com.example.beehives.view.dialogs.RenameApiaryDialog
import com.example.beehives.viewModels.BaseViewModel
import com.example.beehives.viewModels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_about_apiary.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AboutApiaryFragment : Fragment() {

    @Inject lateinit var viewModel : BaseViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel
    private lateinit var navController : NavController
    private lateinit var binding: FragmentAboutApiaryBinding

    val RENAME_APIARY_ARG1 = "current_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
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

        viewModel.currentApiary.observe(viewLifecycleOwner, Observer { apiary ->

            val latLng = apiary.location!!.split(SEPARATOR)
            lat.text = getString(R.string.latitude, latLng[0])
            lng.text = getString(R.string.longitude, latLng[1])

            when (apiary.location) {
                null, EMPTY, SEPARATOR -> {
                    locationImageView.setOnClickListener {
                        sharedViewModel.mapRequest = ADD_MAP_REQUEST
                        navController.navigate(R.id.action_aboutApiaryFragment_to_mapsFragment)
                    }
                }
                else -> {
                    locationImageView.setOnClickListener {
                        sharedViewModel.mapRequest = EDIT_MAP_REQUEST
                        navController.navigate(R.id.action_aboutApiaryFragment_to_mapsFragment)
                    }
                }
            }

            buttonEditName.setOnClickListener {
                showRenameApiaryDialog()
            }
        })

        buttonDelApiary.setOnClickListener {
            viewModel.hives.value?.forEach {
                runBlocking {
                    viewModel.getRepo().deleteRevisionsByHiveId(it.id!!)
                    viewModel.getRepo().deleteHiveById(it.id)
                }

            }
            runBlocking {
                viewModel.getRepo().deleteApiary(viewModel.currentApiary.value!!)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val text = data?.getStringExtra("text")
        viewModel.currentApiary.value?.apply { name = text}?.let { viewModel.updateApiary(it) }
    }

    private fun showRenameApiaryDialog() {
        val fragment: DialogFragment = RenameApiaryDialog.newInstance(Bundle()
            .apply { putString(RENAME_APIARY_ARG1, viewModel.currentApiary.value?.name) })
        fragment.setTargetFragment(this, 1)
        fragment.show(requireFragmentManager(), "tag")
    }

}
