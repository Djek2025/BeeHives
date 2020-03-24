package com.example.beehives.view.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.beehives.view.fragments.AboutApiaryFragment
import com.example.beehives.R
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.view.fragments.HivesFragment
import com.example.beehives.view.fragments.MapsFragment
import com.example.beehives.view.fragments.SettingsFragment
import com.example.beehives.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_menu_header.view.*

private const val ARG_FIRST_RUN = "first_run"
private const val ARG_CURRENT_APIARY = "current_apiary"
const val SEPARATOR = "•—•"

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
            settings.edit().putBoolean(ARG_FIRST_RUN, false).apply()
        }else {
            currentApiaryId = settings.getInt(ARG_CURRENT_APIARY, 1)
        }

        navigation_view.setNavigationItemSelectedListener(this)
        navigation_menu_button.setOnClickListener{
            drawer.openDrawer(GravityCompat.START)
        }
        navigation_view.getHeaderView(0).setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AboutApiaryFragment.newInstance(currentApiaryId, viewModel), "AboutApiary")
                .addToBackStack(null)
                .commit()
            onBackPressed()
        }

        if (HivesFragment.INSTANCE == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.container, HivesFragment.newInstance(viewModel, currentApiaryId), "Hives")
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getApiaryByIdLd(currentApiaryId).observe(this, Observer { apiary ->
            navigation_view.getHeaderView(0).header_apiary_name.text = apiary.name
        })

    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        when(item.itemId){
            R.id.navigation_hives -> transaction.replace(R.id.container, HivesFragment.newInstance(viewModel, currentApiaryId))
            R.id.navigation_map -> transaction.setCustomAnimations(R.animator.card_flip_right_enter,
                    R.animator.card_flip_right_exit,
                    R.animator.card_flip_left_enter,
                    R.animator.card_flip_left_exit)
                .replace(R.id.container, MapsFragment.newInstance(currentApiaryId, viewModel, "show"))
                .addToBackStack("map")
            R.id.navigation_graphs -> true
            R.id.navigation_todo_list -> true
            R.id.navigation_scanner -> true
            R.id.navigation_settings -> transaction.setCustomAnimations(R.animator.card_flip_right_enter,
                    R.animator.card_flip_right_exit,
                    R.animator.card_flip_left_enter,
                    R.animator.card_flip_left_exit)
                .replace(R.id.container, SettingsFragment())
                .addToBackStack("settings")
            R.id.navigation_share -> true
            R.id.navigation_about -> true
        }
        drawer.closeDrawer(GravityCompat.START)
        transaction.commit()
        return true
    }
}
