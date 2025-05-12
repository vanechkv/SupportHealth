package com.example.supporthealth.welcome.detailsOnbording.domain.api.repository

interface DetailsOnBordingRepository {

    fun isFirstLaunch(): Boolean
    fun saveFirstLaunch(isLaunch: Boolean)
}