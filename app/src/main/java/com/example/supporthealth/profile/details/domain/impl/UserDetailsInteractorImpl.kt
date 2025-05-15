package com.example.supporthealth.profile.details.domain.impl

import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.profile.details.domain.api.repository.UserDetailsRepository
import com.example.supporthealth.profile.details.domain.models.UserDetails

class UserDetailsInteractorImpl(private val repository: UserDetailsRepository) :
    UserDetailsInteractor {

    override fun saveUserDetails(userDetails: UserDetails) {
        repository.saveUserDetails(userDetails)
    }

    override fun getUserDetails(): UserDetails? {
        return repository.getUserDetails()
    }
}