package com.example.supporthealth.welcome.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.data.repository.SyncRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val syncRepository: SyncRepository
) : ViewModel() {
    fun syncData(userId: String) {
        viewModelScope.launch {
            syncRepository.apply {
                downloadUser(userId)
                downloadMeals(userId)
                downloadMoods(userId)
                downloadSteps(userId)
                downloadWater(userId)
                downloadHabits(userId)
                downloadProducts(userId)
                downloadMealProducts(userId)
                downloadNutrition(userId)
            }
        }
    }
}