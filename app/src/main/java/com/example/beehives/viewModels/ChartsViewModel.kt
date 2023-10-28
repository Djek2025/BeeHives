package com.example.beehives.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.model.repositories.MainRepository

class ChartsViewModel(): ViewModel() {

    lateinit var repository: MainRepository

    fun getHiveRevisions(hiveId: Int) = repository.getHiveRevisions(hiveId)

}