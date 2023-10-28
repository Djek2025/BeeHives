package com.example.beehives

import android.app.Application
import com.example.beehives.di.DaggerComponent
import com.example.beehives.di.DaggerDaggerComponent
import com.example.beehives.di.MainModule
import com.example.beehives.di.VMModule
import javax.inject.Singleton

@Singleton
class App : Application() {

    private lateinit var appComponent: DaggerComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerDaggerComponent
            .builder()
            .mainModule(MainModule(this))
            .vMModule(VMModule())
            .build()
    }

    fun getComponent() = appComponent
}