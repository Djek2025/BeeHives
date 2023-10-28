package com.example.beehives.view.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.view.adapters.ApiaryAdapter
import com.example.beehives.view.adapters.BeeQueenAdapter
import com.example.beehives.viewModels.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_select_apiary_or_bee_queen.view.*
import kotlinx.android.synthetic.main.fragment_about_hive.*
import javax.inject.Inject


class SelectApiary : DialogFragment(), ApiaryAdapter.Callback {

    @Inject lateinit var viewModel : BaseViewModel
    private val adapter by lazy { ApiaryAdapter(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = layoutInflater.inflate(R.layout.dialog_select_apiary_or_bee_queen, null)
        view.recycler.adapter = adapter
        viewModel.apiaries.observe(this) {
            adapter.setData(it)
        }

        return MaterialAlertDialogBuilder(requireActivity(), R.style.RoundShapeTheme)
            .setView(view)
            .setTitle("Select apiary")
            .setNegativeButton("CLOSE"){ _, _ -> }
            .create()
    }

    override fun onItemClick(item: Apiary) {
        item.id?.let { viewModel.setCurrentApiaryId(it) }
        dismiss()
    }

    override fun onItemLongClick(item: Apiary) {

    }

    override fun onAddNewClick(item: View) {
        viewModel.insertApiary(Apiary(name = "Apiary" + (viewModel.apiaries.value!!.size + 1).toString()))
    }
}

