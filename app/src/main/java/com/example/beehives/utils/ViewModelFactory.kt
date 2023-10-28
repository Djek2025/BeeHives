package com.example.beehives.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.viewModels.*

class ViewModelFactory(private val application: Application, private val repository: MainRepository)
    : ViewModelProvider.NewInstanceFactory() {

//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
//        modelClass.isAssignableFrom(BaseViewModel::class.java) ->
//            BaseViewModel(
//                application = application,
//                repository = repository
//            ) as T
//        modelClass.isAssignableFrom(AboutHiveViewModel::class.java) ->
//            AboutHiveViewModel(
//                application = application,
//                repository = repository
//            ) as T
//        modelClass.isAssignableFrom(HivesViewModel::class.java) ->
//            HivesViewModel(
//                application = application,
//                repository = repository
//            ) as T
//        modelClass.isAssignableFrom(MapsViewModel::class.java) ->
//            MapsViewModel(
//                application = application,
//                repository = repository
//            ) as T
//        modelClass.isAssignableFrom(RevisionViewModel::class.java) ->
//            RevisionViewModel(
//                application = application,
//                repository = repository
//            ) as T
//        modelClass.isAssignableFrom(ScanViewModel::class.java) ->
//            ScanViewModel(
//                application = application,
//                repository = repository
//            ) as T
//        modelClass.isAssignableFrom(ChartsViewModel::class.java) ->
//            ChartsViewModel(
//                application = application,
//                repository = repository
//            ) as T
//        else -> throw IllegalArgumentException()
//    }
}
