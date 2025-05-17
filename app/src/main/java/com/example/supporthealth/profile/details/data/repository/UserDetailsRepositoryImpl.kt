package com.example.supporthealth.profile.details.data.repository

import android.content.SharedPreferences
import com.example.supporthealth.profile.details.data.storage.UserStorage
import com.example.supporthealth.profile.details.domain.api.repository.UserDetailsRepository
import com.example.supporthealth.profile.details.domain.models.ActivityLevel
import com.example.supporthealth.profile.details.domain.models.Gender
import com.example.supporthealth.profile.details.domain.models.GoalType
import com.example.supporthealth.profile.details.domain.models.UserDetails

class UserDetailsRepositoryImpl(private val userStorage: UserStorage) : UserDetailsRepository {

    override fun saveUserDetails(userDetails: UserDetails) {
        userStorage.saveUserDetails(userDetails)
    }

    override fun getUserDetails(): UserDetails? {
        return userStorage.getUserDetails()
    }
}