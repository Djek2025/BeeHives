package com.example.beehives.cache

import android.content.SharedPreferences
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Singleton
class SPCache(private val sp: SharedPreferences) {

    fun getSP() = sp

    companion object{

        internal const val ARG_FIRST_RUN = "first_run"
        internal const val ARG_FIRST_RUN_DEF = true

        internal const val ARG_CURRENT_APIARY = "current_apiary"
        internal const val ARG_CURRENT_APIARY_DEF = 1

        internal const val ARG_LANGUAGE = "language"
        internal const val ARG_LANGUAGE_DEF = "en"

        internal const val ARG_NIGHT_MODE = "night_mode"
        internal const val ARG_NIGHT_MODE_DEF = false

        internal const val ARG_UNITS = "units"
        internal const val ARG_UNITS_DEF = "metric"

        internal const val ARG_BREED = "breed"
        internal const val ARG_DEF_BREED = "Golden Italian"
    }

    var isFirstLaunch
        get() = sp.getBoolean(ARG_FIRST_RUN, ARG_FIRST_RUN_DEF)
        set(value) = sp.edit().putBoolean(ARG_FIRST_RUN, value).apply()

    var currentApiaryId
        get() = sp.getInt(ARG_CURRENT_APIARY, ARG_CURRENT_APIARY_DEF)
        set(value) = sp.edit().putInt(ARG_CURRENT_APIARY, value).apply()

    var language
        get() = sp.getString(ARG_LANGUAGE, ARG_LANGUAGE_DEF)
        set(value) = sp.edit().putString(ARG_LANGUAGE, value).apply()

    var breed
        get() = sp.getString(ARG_BREED, ARG_DEF_BREED)
        set(value) = sp.edit().putString(ARG_BREED, value).apply()

    var nightMode
        get() = sp.getBoolean(ARG_NIGHT_MODE, ARG_NIGHT_MODE_DEF)
        set(value) = sp.edit().putBoolean(ARG_NIGHT_MODE, value).apply()

    var units
        get() = sp.getString(ARG_UNITS, ARG_UNITS_DEF)
        set(value) = sp.edit().putString(ARG_UNITS, value).apply()
}