package com.example.beehives.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.beehives.R
import com.example.beehives.databinding.ActivityMainBinding
import com.example.beehives.viewModel.BaseViewModel
import kotlinx.android.synthetic.main.navigation_menu_header.view.*

const val SEPARATOR = "•—•"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : BaseViewModel
    private lateinit var navController : NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val finalHost = NavHostFragment.create(R.navigation.main_navigation_graph, bundleOf("current_apiary_id" to currentApiaryId))
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, finalHost)
//            .setPrimaryNavigationFragment(finalHost) // equivalent to app:defaultNavHost="true"
//            .commit()

        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(binding.navigationView, navController)

        binding.navigationMenuButton.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.START)
        }

        binding.navigationView.getHeaderView(0).setOnClickListener{
            navController.navigate(R.id.aboutApiaryFragment)
            onBackPressed()
        }

        binding.navigationView.getHeaderView(0).let {
            viewModel.getCurrentApiary().observe(this, Observer { apiary ->
                it.header_apiary_name.text = apiary.name
            })
        }
        binding.navigationView.getHeaderView(0).let {
            viewModel.getCurrentApiaryHives().observe(this, Observer { hives ->
                it.header_hives_count.text = getString(R.string.hives_count, hives.size)
            })
        }

//        navController.navigate(R.id.hivesFragment, bundleOf("current_apiary_id" to currentApiaryId))
    }

    override fun onBackPressed() {
        if(binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }
}
