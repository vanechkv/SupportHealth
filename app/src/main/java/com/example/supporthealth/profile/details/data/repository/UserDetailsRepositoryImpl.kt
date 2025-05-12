package com.example.supporthealth.profile.details.data.repository

import android.content.SharedPreferences
import com.example.supporthealth.profile.details.domain.api.repository.UserDetailsRepository
import com.example.supporthealth.profile.details.domain.models.ActivityLevel
import com.example.supporthealth.profile.details.domain.models.Gender
import com.example.supporthealth.profile.details.domain.models.GoalType
import com.example.supporthealth.profile.details.domain.models.UserDetails

class UserDetailsRepositoryImpl(private val prefs: SharedPreferences) : UserDetailsRepository {
    override fun saveUserData(userDetails: UserDetails) {
        prefs.edit()
            .putString("surname", userDetails.surname)
            .putString("name", userDetails.name)
            .putString("patronymic", userDetails.patronymic)
            .putString("gender", userDetails.gender.name)
            .putInt("height", userDetails.height)
            .putInt("weight", userDetails.weight)
            .putString("birthday", userDetails.birthday)
            .putString("mobility", userDetails.mobility.name)
            .putString("target", userDetails.target.name)
            .apply()
    }

    override fun loadUserDate(): UserDetails {
        return UserDetails(
            surname = prefs.getString("surname", "") ?: "",
            name = prefs.getString("name", "") ?: "",
            patronymic = prefs.getString("patronymic", "") ?: "",
            gender = safeEnumValueOf(prefs.getString("gender", null), Gender.MALE),
            height = prefs.getInt("height", 150),
            weight = prefs.getInt("weight", 50),
            birthday = prefs.getString("birthday", "") ?: "",
            mobility = safeEnumValueOf(prefs.getString("mobility", null), ActivityLevel.LOW),
            target = safeEnumValueOf(prefs.getString("target", null), GoalType.MAINTAIN)
        )
    }

    inline fun <reified T : Enum<T>> safeEnumValueOf(name: String?, default: T): T {
        return try {
            enumValueOf<T>(name ?: "")
        } catch (e: Exception) {
            default
        }
    }
}