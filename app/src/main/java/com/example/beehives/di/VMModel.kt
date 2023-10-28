package com.example.beehives.di

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.example.beehives.cache.SPCache
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.repositories.MainRepository
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.viewModels.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
class VMModule() {

    @Provides
    @Singleton
    fun provideBaseVM(application: Application, repository: MainRepository, settings: SPCache): BaseViewModel =
        ViewModelProvider.NewInstanceFactory().create(BaseViewModel::class.java).apply {
            this.application = application
            this.repository = repository
            this.settings = settings

            if (this.settings.isFirstLaunch) {
                runBlocking { insertApiary(Apiary()) }
                this.settings.isFirstLaunch = false
            }

            this.currentApiaryId.value = settings.currentApiaryId

            this.repository.getApiariesLd().observeForever {
                apiaries.value = it
            }

            this.currentApiaryId.observeForever {

                this.repository.getApiaryHives(it).observeForever {
                    hives.value = it
                }

                this.repository.getApiaryByIdLd(currentApiaryId.value!!).observeForever {
                    currentApiary.value = it
                }
            }

        }

    @Provides
    @Singleton
    fun provideSharedVM(): SharedViewModel =
        ViewModelProvider.NewInstanceFactory().create(SharedViewModel::class.java)

    @Provides
    @Singleton
    fun provideAboutHiveVM(repository: MainRepository): AboutHiveViewModel =
        ViewModelProvider.NewInstanceFactory().create(AboutHiveViewModel::class.java).apply {
            this.repository = repository
        }

    @Provides
    @Singleton
    fun provideChartsVM(repository: MainRepository): ChartsViewModel =
        ViewModelProvider.NewInstanceFactory().create(ChartsViewModel::class.java).apply {
            this.repository = repository
        }

    @Provides
    @Singleton
    fun provideHivesVM(): HivesViewModel =
        ViewModelProvider.NewInstanceFactory().create(HivesViewModel::class.java)

    @Provides
    @Singleton
    fun provideMapsVM(): MapsViewModel =
        ViewModelProvider.NewInstanceFactory().create(MapsViewModel::class.java)

    @Provides
    @Singleton
    fun provideRevisionVM(repository: MainRepository): RevisionViewModel =
        ViewModelProvider.NewInstanceFactory().create(RevisionViewModel::class.java).apply {
            this.repository = repository
        }

    @Provides
    @Singleton
    fun provideScanVM(repository: MainRepository): ScanViewModel =
        ViewModelProvider.NewInstanceFactory().create(ScanViewModel::class.java).apply {
            this.repository = repository
        }


//    @Provides
//    @Singleton
//    fun provideFactory(application: Application): ViewModelFactory =
//        InjectorUtils.provideViewModelFactory(application)

}