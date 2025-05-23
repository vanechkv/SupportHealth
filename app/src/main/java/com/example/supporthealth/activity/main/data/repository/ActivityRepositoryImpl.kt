package com.example.supporthealth.activity.main.data.repository

import com.example.supporthealth.activity.main.domain.api.repository.ActivityRepository
import com.example.supporthealth.main.domain.api.StepDao
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivityRepositoryImpl(
    private val userDetailsInteractor: UserDetailsInteractor,
    private val stepDao: StepDao
): ActivityRepository {
    override suspend fun updateStep() {
        val target = userDetailsInteractor.getUserDetails()!!.targetActivity
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val todayStr = today.format(formatter)
        val step = stepDao.getStepsByDate(todayStr)
        val update = step!!.copy(
            target = target
        )
        stepDao.update(update)
    }
}