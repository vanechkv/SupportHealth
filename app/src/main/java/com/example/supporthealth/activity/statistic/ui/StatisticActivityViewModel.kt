package com.example.supporthealth.activity.statistic.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.supporthealth.main.domain.api.StepDao
import com.example.supporthealth.main.domain.models.StepEntity

class StatisticActivityViewModel(
    private val stepDao: StepDao
) : ViewModel() {

    fun observeStepDate(date: String): LiveData<StepEntity> {
        return stepDao.getStepByDate(date).asLiveData()
    }

    fun observeSteps(): LiveData<MutableList<StepEntity>> {
        return stepDao.observeAllSteps().asLiveData()
    }
}