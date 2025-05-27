package com.example.supporthealth.profile.details.data.storage

import android.content.SharedPreferences
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.profile.details.domain.models.UserDetails
import com.google.gson.Gson

class UserStorage(
    private val pref: SharedPreferences,
    private val gson: Gson
) {

    companion object {
        private const val USER_DETAILS = "user_details"
    }

    fun saveUserDetails(userDetails: UserDetails) {
        pref.edit()
            .putString(USER_DETAILS, createJsonFromUserDetails(userDetails))
            .apply()
    }

    fun getUserDetails(): UserDetails? {
        val json = pref.getString(USER_DETAILS, null)
        return createUserDetailsFromJson(json)
    }

    fun deleteUserDetails() {
        pref.edit().remove(USER_DETAILS).apply()
    }

    private fun createJsonFromUserDetails(userDetails: UserDetails): String {
        return gson.toJson(userDetails)
    }

    private fun createUserDetailsFromJson(json: String?): UserDetails? {
        return gson.fromJson(json, UserDetails::class.java)
    }
}