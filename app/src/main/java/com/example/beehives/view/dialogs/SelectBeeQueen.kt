package com.example.beehives.view.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.model.db.entities.BeeQueen
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.view.adapters.BeeQueenAdapter
import com.example.beehives.viewModels.BaseViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_select_apiary_or_bee_queen.view.*
import java.util.*
import javax.inject.Inject

class SelectBeeQueenDialog : DialogFragment(), BeeQueenAdapter.Callback {

    @Inject lateinit var viewModel : BaseViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel
    private val adapter by lazy { BeeQueenAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)

        viewModel.repository.getFreeBeeQueen().observe(this){
            adapter.setData(it)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_select_apiary_or_bee_queen, null)
        view.recycler.adapter = adapter

        return MaterialAlertDialogBuilder(requireActivity(), R.style.RoundShapeTheme)
            .setView(view)
            .setTitle("Bee Queen")
            .setNegativeButton("Cancel"){ _, _ ->

            }
            .create()
    }

    override fun onItemClick(item: BeeQueen) {
        viewModel.updateBeeQueen(item.apply {
            hiveId = viewModel.currentHive.value?.id
        })
        viewModel.updateHive(viewModel.currentHive.value?.apply {
            beeQueenId = item.id
        }!!)
        dismiss()
    }

//    override fun onAddNewClick(item: View, itemCount: Int) {
//        viewModel.insertBeeQueen(BeeQueen(name = "Bee Queen $itemCount", year = 2020))
//    }

    override fun onItemLongClick(item: BeeQueen) {

    }
}