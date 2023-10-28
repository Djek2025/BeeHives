package com.example.beehives.view.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.cache.SPCache
import com.example.beehives.databinding.ActivityMainBinding
import com.example.beehives.di.DaggerDaggerComponent
import com.example.beehives.view.fragments.SettingsFragment
import com.example.beehives.viewModels.BaseViewModel
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.view.dialogs.SelectApiary
import com.example.beehives.viewModels.SharedViewModel
import kotlinx.android.synthetic.main.navigation_menu_header.view.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject lateinit var viewModel : BaseViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel
//    @Inject lateinit var factory : ViewModelFactory
    @Inject lateinit var settings: SPCache

    private lateinit var navController : NavController
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        (application as App).getComponent().inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        settings.getSP().registerOnSharedPreferenceChangeListener(this)


//        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat
            .setOnApplyWindowInsetsListener(binding.toolbar) { view, insets ->
                view.updatePadding(top = insets.systemWindowInsetTop)
                sharedViewModel.liveInsets.value =
                    mapOf("top" to insets.systemWindowInsetTop,
                        "bottom" to insets.systemWindowInsetBottom,
                        "left" to insets.systemWindowInsetLeft,
                        "right" to insets.systemWindowInsetRight,)
                insets
            }

        binding.vm = viewModel

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    override fun onResume() {
        super.onResume()
        initListeners()
    }

    override fun onBackPressed() {
        if(binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        SettingsFragment().onSharedPreferenceChanged(sharedPreferences, key)
    }

    private fun initListeners(){
        binding.navView.getHeaderView(0).setOnClickListener{
            navController.navigate(R.id.aboutApiaryFragment); onBackPressed()
        }
        binding.navView.getHeaderView(0).change_apiary_btn.setOnClickListener {
            SelectApiary().show(supportFragmentManager, "tag")
        }
        viewModel.currentApiary.observeForever {
            binding.navView.getHeaderView(0).header_apiary_name.text = it.name
        }
        viewModel.hives.observeForever {
            binding.navView.getHeaderView(0).header_hives_count.text = getString(R.string.hives_count, it.size)
        }
        binding.navigationMenuButton.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.START)
        }
    }
}
