package com.example.supporthealth.welcome.detailsOnbording.data.repository

import com.example.supporthealth.welcome.detailsOnbording.data.storage.DetailsOnBordingStorage
import com.example.supporthealth.welcome.detailsOnbording.domain.api.repository.DetailsOnBordingRepository

class DetailsOnBordingRepositoryImpl(private val storage: DetailsOnBordingStorage) : DetailsOnBordingRepository {

    override fun isFirstLaunch(): Boolean {
        return storage.getIsFirstLaunch()
    }

    override fun saveFirstLaunch(isLaunch: Boolean) {
        storage.saveFirstLaunch(isLaunch)
    }
}