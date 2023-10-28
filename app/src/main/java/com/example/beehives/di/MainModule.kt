package com.example.beehives.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.preference.PreferenceManager
import com.example.beehives.cache.SPCache
import com.example.beehives.model.db.MainDatabase
import com.example.beehives.model.db.dao.GeneralDao
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.view.activities.MainActivity
import com.example.beehives.viewModels.SharedViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule (private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideDao(application: Application): GeneralDao = MainDatabase.getDatabase(application).generalDao()

    @Provides
    @Singleton
    fun provideRepository(dao: GeneralDao): MainRepository = MainRepository(dao)

    @Provides
    @Singleton
    fun provideSettings(sp: SharedPreferences): SPCache = SPCache(sp)

    @Provides
    @Singleton
    fun provideSharedPref(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}