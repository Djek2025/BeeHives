package com.example.beehives.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.beehives.model.db.entities.Hive

class HivesViewModel(application: Application): BaseViewModel(application) {

    val checked: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }

    val checkedHives: MutableLiveData<Hive> by lazy { MutableLiveData<Hive>() }

}