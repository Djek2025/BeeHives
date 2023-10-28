package com.example.beehives.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.databinding.FragmentAddRevisionBinding
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.SEPARATOR
import com.example.beehives.utils.SEPARATOR_SECONDARY
import com.example.beehives.viewModels.RevisionViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.utils.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_add_revision.*
import kotlinx.android.synthetic.main.item_for_enter_frames_count.view.*
import java.util.*
import javax.inject.Inject

class AddRevisionFragment : Fragment(), SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener {

    @Inject lateinit var viewModel : RevisionViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel
    private lateinit var binding: FragmentAddRevisionBinding

    private val builder = MaterialDatePicker.Builder.datePicker()
    private val datePicker = builder.build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
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

        sharedViewModel.liveInsets.observe(viewLifecycleOwner){
            binding.scrollView.updatePadding(bottom = it.getValue("bottom"))
        }

        viewModel.lastRevision.value = viewModel.getLastRev(sharedViewModel.selectedHiveId)

        viewModel.lastRevision.value?.frames?.split(SEPARATOR_SECONDARY)?.forEach {
            if (it.isNotEmpty()){
                viewModel.bodies.add(it)
            }
        }

        if ( viewModel.bodies.isNotEmpty()) {
            viewModel.bodies.forEach {
                val frames = it.split(SEPARATOR)
                if (it.isNotEmpty()) {
                    binding.rootLinear.addView(layoutInflater.inflate(R.layout.item_for_enter_frames_count, rootLinear, false)
                        .apply {
                            tag = "new_views"
                                editTextFramesInstall.text = SpannableStringBuilder(frames[0])
                                editTextFramesInstallOf.text = SpannableStringBuilder(frames[1])
                                editTextFrameSize.text = SpannableStringBuilder(frames[2])
                        }
                    )
                }
            }
        } else {
            binding.rootLinear.addView(layoutInflater.inflate(R.layout.item_for_enter_frames_count, rootLinear, false).apply {
                tag = "new_views"
            })
            viewModel.bodies.add(String())
        }

        floatingActionButton2.setOnClickListener {
            viewModel.insertRevision(Revision(
                hiveId = sharedViewModel.selectedHiveId,
                date = viewModel.getDate(),
                strength = seekBar.progress,
                frames = getFramesStr(),
                note = noteEditText.text.toString())
            )
            activity?.onBackPressed()
        }

        selectDateBtn.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                viewModel.setDate(it)
                selectDate.text = "${calendar.get(Calendar.DAY_OF_MONTH)}." +
                        "${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.YEAR)}"
            }
            datePicker.show(requireFragmentManager(), "MyTAG")
        }

        binding.buttonAddFrameField.setOnClickListener {
            binding.rootLinear.addView(layoutInflater.inflate(R.layout.item_for_enter_frames_count, rootLinear, false).apply { tag = "new_views" })
            viewModel.bodies.add(String())
        }

        binding.buttonDelFrameField.setOnClickListener {
            val fd = view?.findViewWithTag<ViewGroup>("new_views")
            binding.rootLinear.removeView(fd)
            viewModel.bodies.removeLast()
        }
    }

    private fun getFramesStr(): String{
        var str = ""
        for (i in 0..viewModel.bodies.count()){
            val fd = view?.findViewWithTag<ViewGroup>("new_views")
            if (fd != null) {
                str += fd.editTextFramesInstall.text.toString() + SEPARATOR
                str += fd.editTextFramesInstallOf.text.toString() + SEPARATOR
                str += fd.editTextFrameSize.text.toString() + SEPARATOR_SECONDARY
            }
            binding.rootLinear.removeView(fd)
        }
        return str
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
