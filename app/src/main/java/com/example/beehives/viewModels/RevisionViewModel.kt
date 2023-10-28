package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.TimeConverter
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RevisionViewModel() : ViewModel() {

    lateinit var repository: MainRepository

    val defStr = "60%"
    var bodies = mutableListOf<String>()
    private var date: Long? = null
    private val timeConverter = TimeConverter()
    val lastRevision: MutableLiveData<Revision> by lazy { MutableLiveData<Revision>() }

    init {
        date = timeConverter.getTimeLong()
    }

    fun getDate() = date

    fun setDate(time: Long) {date = time}

    fun getDateStr() = timeConverter.longToString(timeConverter.getTimeLong())

    fun getLastRev(hiveId: Int) = repository.getLastRevisionByHiveId(hiveId)

    fun insertRevision(revision: Revision) = viewModelScope.launch { repository.insertRevision(revision) }

}
