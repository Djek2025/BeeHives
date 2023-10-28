package com.example.beehives.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.databinding.FragmentAboutHiveBinding
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.DEFAULT_PHOTO_HIVE
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.view.adapters.RevisionsAdapter
import com.example.beehives.viewModels.AboutHiveViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.view.dialogs.RenameHiveDialog
import com.example.beehives.view.dialogs.RenameHiveDialog.Companion.RENAME_BREED_ARG
import com.example.beehives.view.dialogs.RenameHiveDialog.Companion.RENAME_BREED_BUNDLE_NAME
import com.example.beehives.view.dialogs.RenameHiveDialog.Companion.RENAME_HIVE_ARG
import com.example.beehives.view.dialogs.RenameHiveDialog.Companion.RENAME_HIVE_BUNDLE_NAME
import com.example.beehives.view.dialogs.SelectBeeQueenDialog
import kotlinx.android.synthetic.main.fragment_about_hive.*
import kotlinx.coroutines.*
import javax.inject.Inject

private const val ARG_PHOTO_REQUEST = 33

class AboutHiveFragment : Fragment(), RevisionsAdapter.Callback {

    @Inject lateinit var viewModelAbout: AboutHiveViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAboutHiveBinding
    private val adapter by lazy { RevisionsAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
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
        binding.revisionsRecycler.adapter = adapter

        sharedViewModel.liveInsets.observe(viewLifecycleOwner){
            binding.revisionsRecycler.updatePadding(bottom = it.getValue("bottom"))
        }


        viewModelAbout.getHive(sharedViewModel.selectedHiveId).observe(viewLifecycleOwner, {
            viewModelAbout.hive.value = it
        })

        viewModelAbout.getHiveRevisions(sharedViewModel.selectedHiveId).observe(viewLifecycleOwner, {
            adapter.setRevisions(it)
        })

        binding.buttonNewRevision.setOnClickListener {
            navController.navigate(R.id.action_aboutHiveFragment_to_addRevisionFragment)
        }
        binding.addLabelButton.setOnClickListener {
            sharedViewModel.scanRequest = "write"
            navController.navigate(R.id.scanFragment)
        }

        binding.buttonAddPhoto.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), ARG_PHOTO_REQUEST)
        }

        binding.buttonBeeQueen.setOnClickListener {
            if (viewModelAbout.hive.value?.beeQueenId == null) {
                sharedViewModel.beeQueenRequest = "select"
                SelectBeeQueenDialog().show(requireActivity().supportFragmentManager, "tag")
            } else {
                sharedViewModel.beeQueenRequest = "add"
                Toast.makeText(context, "Included", Toast.LENGTH_SHORT).show()

            }
        }

        val popupMenu = PopupMenu(context, popUpBtn)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.changeNameBreed -> {
                    showRenameHiveDialog()
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
        } else if (requestCode == 1){
            val textName = data?.getStringExtra(RENAME_HIVE_BUNDLE_NAME)
            val textBreed = data?.getStringExtra(RENAME_BREED_BUNDLE_NAME)
            viewModelAbout.hive.value?.name = textName
            viewModelAbout.hive.value?.breed = textBreed
            viewModelAbout.updateHive()
        }

    }

    private fun showRenameHiveDialog() {
        val fragment: DialogFragment = RenameHiveDialog.newInstance(Bundle()
            .apply {
                putString(RENAME_HIVE_ARG, viewModelAbout.hive.value?.name)
                putString(RENAME_BREED_ARG, viewModelAbout.hive.value?.breed)
            })
        fragment.setTargetFragment(this, 1)
        fragment.show(requireFragmentManager(), "tag")
    }


}
