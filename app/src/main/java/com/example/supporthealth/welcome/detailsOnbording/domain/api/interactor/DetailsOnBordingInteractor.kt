package com.example.supporthealth.welcome.detailsOnbording.domain.api.interactor

interface DetailsOnBordingInteractor {

    fun isFirstLaunch(): Boolean
    fun saveFirstLaunch(isLaunch: Boolean)
}