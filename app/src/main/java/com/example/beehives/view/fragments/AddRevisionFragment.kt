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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.beehives.R
import com.example.beehives.databinding.FragmentAddRevisionBinding
import com.example.beehives.utils.TimeConverter
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.view.activities.SEPARATOR
import com.example.beehives.viewModel.BaseViewModel
import com.example.beehives.viewModel.RevisionViewModel
import com.example.beehives.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add_revision.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddRevisionFragment : Fragment(), SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel : RevisionViewModel
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var binding: FragmentAddRevisionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RevisionViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_revision, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        seekBar.setOnSeekBarChangeListener(this)

        CoroutineScope(Dispatchers.IO).launch {
            val lrev = viewModel.getLastRev(sharedViewModel.selectedHive).await()
            withContext(Dispatchers.Main){
                viewModel.lastRevision.value = lrev
            }
        }

        floatingActionButton2.setOnClickListener {
            viewModel.insertRevison(Revision(
                hiveId = sharedViewModel.selectedHive,
                date = viewModel.getDate(),
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
