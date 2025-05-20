package com.example.supporthealth.activity.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.supporthealth.main.domain.api.StepDao
import com.example.supporthealth.main.domain.models.StepEntity

class ActivityViewModel(
    private val stepDao: StepDao
) : ViewModel() {

    fun observeStep(): LiveData<MutableList<StepEntity>> {
       return stepDao.observeAllSteps().asLiveData()
    }
}