package com.example.supporthealth.welcome.detailsOnbording.domain.impl

import com.example.supporthealth.welcome.detailsOnbording.domain.api.interactor.DetailsOnBordingInteractor
import com.example.supporthealth.welcome.detailsOnbording.domain.api.repository.DetailsOnBordingRepository

class DetailsOnBordingInteractorImpl(private val repository: DetailsOnBordingRepository) : DetailsOnBordingInteractor {

    override fun isFirstLaunch(): Boolean {
        return repository.isFirstLaunch()
    }

    override fun saveFirstLaunch(isLaunch: Boolean) {
        repository.saveFirstLaunch(isLaunch)
    }
}