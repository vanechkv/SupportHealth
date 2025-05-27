package com.example.supporthealth.profile.details.domain.api.repository

import com.example.supporthealth.profile.details.domain.models.UserDetails

interface UserDetailsRepository {

    fun saveUserDetails(userDetails: UserDetails)

    fun getUserDetails() : UserDetails?

    fun deleteUserDetails()
}