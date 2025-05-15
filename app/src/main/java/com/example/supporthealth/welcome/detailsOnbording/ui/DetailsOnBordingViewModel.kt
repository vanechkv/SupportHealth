package com.example.supporthealth.welcome.detailsOnbording.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.profile.details.domain.models.UserDetails
import com.example.supporthealth.welcome.detailsOnbording.domain.api.interactor.DetailsOnBordingInteractor
import kotlinx.coroutines.launch

class DetailsOnBordingViewModel(
    private val detailsOnBordingInteractor: DetailsOnBordingInteractor,
    private val nutritionInteractor: NutritionInteractor,
    private val userDetailsInteractor: UserDetailsInteractor
) :
    ViewModel() {

    fun recalculateNorm(date: String, user: UserDetails) {
        viewModelScope.launch {
            nutritionInteractor.calculate(user)
        }
    }

    fun saveData(userDetails: UserDetails) {
        userDetailsInteractor.saveUserDetails(userDetails)
    }

    fun saveIsFirstLaunch(isLaunch: Boolean) {
        detailsOnBordingInteractor.saveFirstLaunch(isLaunch)
    }
}