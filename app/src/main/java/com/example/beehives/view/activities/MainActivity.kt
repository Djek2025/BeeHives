package com.example.beehives.view.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.beehives.R
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.view.fragments.HivesFragment
import com.example.beehives.view.fragments.MapsFragment
import com.example.beehives.view.fragments.SettingsFragment
import com.example.beehives.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

private const val ARG_FIRST_RUN = "first_run"
private const val ARG_CURRENT_APIARY = "current_apiary"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer : DrawerLayout
    private lateinit var settings : SharedPreferences
    private lateinit var viewModel : MainViewModel
    private var currentApiaryId : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        settings = PreferenceManager.getDefaultSharedPreferences(this)
        drawer = findViewById(R.id.drawer)

        if(settings.getBoolean(ARG_FIRST_RUN, true)){
            viewModel.insertApiary(Apiary(name = "My first apiary"))
            Toast.makeText(this,"First Apiary", Toast.LENGTH_SHORT).show()
            settings.edit().putBoolean(ARG_FIRST_RUN, false).apply()
        }else {
            currentApiaryId = settings.getInt(ARG_CURRENT_APIARY, 1)
        }

        findViewById<NavigationView>(R.id.navigation_view).setNavigationItemSelectedListener(this)
        navigation_menu_button.setOnClickListener{
            drawer.openDrawer(GravityCompat.START)
        }

//        supportFragmentManager.findFragmentByTag("Hives")?.isAdded
        if (HivesFragment.INSTANCE == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, HivesFragment.newInstance(viewModel, currentApiaryId), "Hives")
                .commit()
        }
    }

    override fun onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_hives -> supportFragmentManager.beginTransaction()
                .replace(R.id.container, HivesFragment.newInstance(viewModel, currentApiaryId))
                .commit()
            R.id.navigation_map -> supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_enter,
                    R.animator.card_flip_right_exit,
                    R.animator.card_flip_left_enter,
                    R.animator.card_flip_left_exit)
                .replace(R.id.container, MapsFragment())
                .addToBackStack("map")
                .commit()
            R.id.navigation_graphs -> true
            R.id.navigation_todo_list -> true
            R.id.navigation_scanner -> true
            R.id.navigation_settings -> supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_enter,
                    R.animator.card_flip_right_exit,
                    R.animator.card_flip_left_enter,
                    R.animator.card_flip_left_exit)
                .replace(R.id.container, SettingsFragment())
                .addToBackStack("settings")
                .commit()
            R.id.navigation_share -> true
            R.id.navigation_about -> true
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
