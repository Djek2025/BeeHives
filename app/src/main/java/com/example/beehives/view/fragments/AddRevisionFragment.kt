package com.example.beehives.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.example.beehives.R
import com.example.beehives.utils.TimeConverter
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.viewModel.BaseViewModel
import kotlinx.android.synthetic.main.fragment_add_revision.*

private const val ARG_HIVE_ID = "current_hive_id"

class AddRevisionFragment : Fragment(), SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener {

    private var hiveId: Int? = null
    private lateinit var viewModel : BaseViewModel

    companion object {
        @JvmStatic
        fun newInstance() =
            AddRevisionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        arguments?.let {
            hiveId = it.getInt(ARG_HIVE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_revision, container, false)
    }

    private var date: Long? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        seekBar.setOnSeekBarChangeListener(this)

        val timeConverter = TimeConverter()
        date = timeConverter.getTimeLong()
        selectDate.text = timeConverter.longToString(timeConverter.getTimeLong())

        floatingActionButton2.setOnClickListener {
            viewModel.insertRevison(Revision(
                hiveId = hiveId,
                date = date,
                strength = seekBar.progress,
                frames = getFramesStr(),
                note = noteEditText.text.toString())
            )
            activity?.onBackPressed()
        }

//        selectDate.setOnClickListener {
//            DatePickerDialog(context!!, this,
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)).show()
//        }
    }

    fun getFramesStr(): String{
        return editTextFramesInstall.text.toString() + SEPARATOR +
                editTextFramesInstallOf.text.toString() + SEPARATOR +
                editTextFrameSize.text.toString()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        percents.text = "$progress%"
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}
