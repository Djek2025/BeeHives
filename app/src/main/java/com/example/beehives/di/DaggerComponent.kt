package com.example.beehives.di

import com.example.beehives.view.activities.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface DaggerComponent {
    fun inject(target: MainActivity)
}