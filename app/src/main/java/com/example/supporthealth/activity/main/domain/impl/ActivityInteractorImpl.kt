package com.example.supporthealth.activity.main.domain.impl

import com.example.supporthealth.activity.main.domain.api.interactor.ActivityInteractor
import com.example.supporthealth.activity.main.domain.api.repository.ActivityRepository

class ActivityInteractorImpl(private val repository: ActivityRepository): ActivityInteractor {

    override suspend fun updateStep() {

        repository.updateStep()
    }
}