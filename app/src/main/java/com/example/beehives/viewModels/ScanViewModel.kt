package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.beehives.model.repositories.MainRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScanViewModel(application: Application,private val repository: MainRepository) : BaseViewModel(application, repository) {

    fun getHiveIdByLabelAsync(label: String) = viewModelScope.async{ repository.getHiveIdByLabel(label) }

    fun setLabelByHiveId(id: Int, label: String) = viewModelScope.launch { repository.setLabelByHiveId(id, label)}
}