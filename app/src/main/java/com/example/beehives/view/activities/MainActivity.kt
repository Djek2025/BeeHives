package com.example.beehives.view.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.beehives.R
import com.example.beehives.cache.SPCache
import com.example.beehives.databinding.ActivityMainBinding
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.view.fragments.SettingsFragment
import com.example.beehives.viewModels.BaseViewModel
import com.example.beehives.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_menu_header.view.*

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var viewModel : BaseViewModel
    private lateinit var factory : ViewModelFactory
    private lateinit var navController : NavController
    private lateinit var binding : ActivityMainBinding
    private lateinit var settings: SPCache

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        settings = SPCache(PreferenceManager.getDefaultSharedPreferences(this)
            .apply { registerOnSharedPreferenceChangeListener(this@MainActivity) })

        val apiaryId: Int = if (settings.isFirstLaunch){
            settings.isFirstLaunch = false; 1
        }else{
            settings.currentApiaryId
        }

        factory = InjectorUtils.provideViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(BaseViewModel::class.java)

        binding.vm = viewModel


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)

    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        SettingsFragment().onSharedPreferenceChanged(sharedPreferences, key)
    }

    private fun initListeners(){
        navView.getHeaderView(0).setOnClickListener{
            navController.navigate(R.id.aboutApiaryFragment)
            onBackPressed()
        }
        viewModel.currentApiary.observeForever {
            navView.getHeaderView(0).header_apiary_name.text = it.name
        }
        viewModel.currentApiaryHives.observeForever {
            navView.getHeaderView(0).header_hives_count.text = getString(R.string.hives_count, it.size)
        }
        binding.navigationMenuButton.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.START)
        }
    }
}
