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
import com.example.beehives.R
import com.example.beehives.databinding.ActivityMainBinding
import com.example.beehives.view.fragments.SettingsFragment
import com.example.beehives.viewModel.BaseViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_menu_header.view.*


const val SEPARATOR = "•—•"
const val DEFAULT_PHOTO_HIVE = "android.resource://com.example.beehives/drawable/hive"

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var viewModel : BaseViewModel
    private lateinit var navController : NavController
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        viewModel.getSettings().registerOnSharedPreferenceChangeListener(this)
        binding.vm = viewModel

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)

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

    fun initListeners(){
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
