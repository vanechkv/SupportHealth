package com.example.supporthealth.profile.details.domain.api.interactor

import com.example.supporthealth.profile.details.domain.models.UserDetails

interface UserDetailsInteractor {

    fun saveUserData(userDetails: UserDetails)

    fun loadUserDate() : UserDetails
}