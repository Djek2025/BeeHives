package com.example.beehives.di

import com.example.beehives.view.activities.MainActivity
import com.example.beehives.view.dialogs.SelectApiary
import com.example.beehives.view.dialogs.SelectBeeQueenDialog
import com.example.beehives.view.fragments.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class, VMModule::class])
interface DaggerComponent {

    fun inject(target: MainActivity)

    fun inject(target: AboutApiaryFragment)
    fun inject(target: AboutHiveFragment)
    fun inject(target: AddRevisionFragment)
    fun inject(target: BeeQueensFragment)
    fun inject(target: ChartsFragment)
    fun inject(target: HivesFragment)
    fun inject(target: MapsFragment)
    fun inject(target: ScanFragment)
    fun inject(target: SettingsFragment)
    fun inject(target: ShareFragment)

    fun inject(target: SelectBeeQueenDialog)
    fun inject(target: SelectApiary)

}