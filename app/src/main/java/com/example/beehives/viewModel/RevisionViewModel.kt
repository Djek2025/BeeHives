package com.example.beehives.viewModel

import android.app.Application
import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.EditText
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.TimeConverter
import com.example.beehives.view.activities.SEPARATOR
import kotlinx.coroutines.async

class RevisionViewModel(application: Application) : BaseViewModel(application) {

    val defStr = "60%"
    private val repository = getRepo()
    val lastRevision: MutableLiveData<Revision> by lazy { MutableLiveData<Revision>() }
    private var date: Long? = null
    private val timeConverter = TimeConverter()

    init {
        date = timeConverter.getTimeLong()
    }

    fun getDate() = timeConverter.getTimeLong()
    fun getDateStr() = timeConverter.longToString(timeConverter.getTimeLong())

    fun getLastRev(hiveId: Int) = viewModelScope.async { repository.getLastRevisionByHiveId(hiveId) }

}
