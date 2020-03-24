package com.example.beehives.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar

import com.example.beehives.R
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_revision.*
import java.util.*

private const val ARG_HIVE_ID = "curent_hive_id"

class AddRevisionFragment : Fragment(), SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener {
    private var hiveId: Int? = null
    private lateinit var viewModel : MainViewModel

    companion object {
        @JvmStatic
        fun newInstance(hiveId: Int,  vModel : MainViewModel) =
            AddRevisionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_HIVE_ID, hiveId)
                }
                viewModel = vModel
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            hiveId = it.getInt(ARG_HIVE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_revision, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        seekBar.setOnSeekBarChangeListener(this)

        val calendar = Calendar.getInstance()
        select_date.text = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                "${calendar.get(Calendar.MONTH + 1)}/" +
                "${calendar.get(Calendar.YEAR)}"
        select_date.setOnClickListener {
            DatePickerDialog(context!!, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        floatingActionButton2.setOnClickListener {
            viewModel.insertRevison(Revision(
                hiveId = hiveId,
                date = 215445216L,
                strength = seekBar.progress,
                frames = "8/12 145mm",
                note = noteEditText.text.toString()))
            activity?.onBackPressed()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        select_date.text = "$dayOfMonth/${month+1}/$year"
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        percents.text = "$progress%"
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}
