package com.example.beehives.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.databinding.FragmentAboutHiveBinding
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.DEFAULT_PHOTO_HIVE
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.view.adapters.RevisionsAdapter
import com.example.beehives.viewModels.AboutHiveViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_about_hive.*
import kotlinx.coroutines.*

private const val ARG_PHOTO_REQUEST = 33

class AboutHiveFragment : Fragment(), RevisionsAdapter.Callback {

    private lateinit var viewModelAbout: AboutHiveViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAboutHiveBinding
    private val adapter by lazy { RevisionsAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SharedViewModel::class.java)
        factory = InjectorUtils.provideViewModelFactory(activity!!.application)
        viewModelAbout = ViewModelProvider(this, factory).get(AboutHiveViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_hive, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModelAbout
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(revisionsRecycler)
        revisionsRecycler.adapter = adapter


        viewModelAbout.getHive(sharedViewModel.selectedHive).observe(viewLifecycleOwner, Observer {
            viewModelAbout.hive.value = it
        })

        viewModelAbout.getHiveRevisions(sharedViewModel.selectedHive).observe(viewLifecycleOwner, Observer {
            adapter.setRevisions(it)
        })

        add_revision_btn.setOnClickListener {
            navController.navigate(R.id.action_aboutHiveFragment_to_addRevisionFragment)
        }
        addLabelButton.setOnClickListener {
            sharedViewModel.scanRequest = "write"
            navController.navigate(R.id.scanFragment)
        }

        buttonAddPhoto.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), ARG_PHOTO_REQUEST)
        }

        val popupMenu = PopupMenu(context, popUpBtn)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.changeNameBreed -> {

                    true
                }
                R.id.setDefaultImage -> {
                    viewModelAbout.setPhoto(DEFAULT_PHOTO_HIVE)
                    true
                }
                R.id.deleteHive -> {
                    CoroutineScope(Dispatchers.Main).launch{
                        viewModelAbout.deleteCurrentHiveAndRevisions()
                        delay(100)
                        navController.popBackStack()
                    }
                    true
                }
                else -> false
            }
        }

        popUpBtn.setOnClickListener{
            popupMenu.show()
        }
    }

    override fun onItemClick(item: Revision) {

    }

    override fun onItemLongClick(item: Revision) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ARG_PHOTO_REQUEST == requestCode && data != null){
            viewModelAbout.setPhoto(data.data.toString())
        }
    }
}
