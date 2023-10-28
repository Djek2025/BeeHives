package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.repositories.MainRepository
import kotlinx.coroutines.launch

class HivesViewModel(): ViewModel() {

    val checked: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }

    val checkedHives: MutableLiveData<Hive> by lazy { MutableLiveData<Hive>() }

}