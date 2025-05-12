package com.example.supporthealth.profile.details.domain.impl

import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.profile.details.domain.api.repository.UserDetailsRepository
import com.example.supporthealth.profile.details.domain.models.UserDetails

class UserDetailsInteractorImpl(private val repository: UserDetailsRepository) :
    UserDetailsInteractor {
    override fun saveUserData(userDetails: UserDetails) {
        repository.saveUserData(userDetails)
    }

    override fun loadUserDate(): UserDetails {
        return repository.loadUserDate()
    }
}