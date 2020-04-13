package com.example.beehives.utils

import android.app.Application
import com.example.beehives.model.db.MainDatabase
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.viewModels.ViewModelFactory

object InjectorUtils {

    fun provideViewModelFactory(application: Application): ViewModelFactory {
        val mainRepository = MainRepository(MainDatabase.getDatabase(application).generalDao())
        return ViewModelFactory(application, mainRepository)
    }

}