package com.example.beehives.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.example.beehives.R

class SettingsFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?){
        when(key){
            "night_mode" -> {
                if(sharedPreferences!!.getBoolean("night_mode", false)){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    activity?.recreate()
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    activity?.recreate()
                }
            }
            "language" -> {
                when(sharedPreferences!!.getString("language", "en")){
                    "en" -> { }
                    "ua" -> { }
                    "ru" -> { }
                }
            }
            "units" -> {}
        }
    }

}