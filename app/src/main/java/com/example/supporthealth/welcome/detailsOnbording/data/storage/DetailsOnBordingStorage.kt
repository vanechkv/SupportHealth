package com.example.supporthealth.welcome.detailsOnbording.data.storage

import android.content.SharedPreferences

class DetailsOnBordingStorage(private val pref: SharedPreferences) {

    fun getIsFirstLaunch() : Boolean {
        return pref.getBoolean("isFirstLaunch", true)
    }

    fun saveFirstLaunch(isLaunch: Boolean) {
        pref.edit()
            .putBoolean("isFirstLaunch", isLaunch)
            .apply()
    }
}