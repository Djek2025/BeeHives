package com.example.beehives.view.activities

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.beehives.R
import com.example.beehives.databinding.ActivityMainBinding
import com.example.beehives.view.fragments.SettingsFragment
import com.example.beehives.viewModel.BaseViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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


        binding.navigationMenuButton.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.START)
        }

        navView.getHeaderView(0).setOnClickListener{
            navController.navigate(R.id.aboutApiaryFragment)
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val config = resources.configuration
        when(key){
            "night_mode" -> {
                if(sharedPreferences!!.getBoolean("night_mode", false)){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    recreate()
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    recreate()
                }
            }
            "language" -> {
                when(sharedPreferences!!.getString("language", "en")){
                    "en" ->{config.setLocale(Locale("en")); recreate()}
                    "ua" ->{config.setLocale(Locale("ua")); recreate()}
                    "ru" ->{config.setLocale(Locale("ru")); recreate()}
                }
            }
            "units" -> {}
        }
    }
}
