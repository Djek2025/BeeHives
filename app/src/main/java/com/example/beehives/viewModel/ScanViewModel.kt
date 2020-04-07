package com.example.beehives.viewModel

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScanViewModel(application: Application) : BaseViewModel(application) {

    private val repository = getRepo()

    fun getHiveIdByLabelAsync(label: String) = viewModelScope.async{ repository.getHiveIdByLabel(label) }
    fun setLabelByHiveId(id: Int, label: String) = viewModelScope.launch { repository.setLabelByHiveId(id, label)}
}