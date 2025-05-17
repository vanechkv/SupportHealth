package com.example.supporthealth.profile.details.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.profile.details.domain.models.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val userDetailsInteractor: UserDetailsInteractor,
    private val nutritionInteractor: NutritionInteractor
) : ViewModel() {

    private val userLiveData = MutableLiveData<UserDetails?>()
    fun observeUser(): LiveData<UserDetails?> = userLiveData

    fun loadData() {
        userLiveData.postValue(userDetailsInteractor.getUserDetails())
    }

    fun saveData(userData: UserDetails) {
        userDetailsInteractor.saveUserDetails(userData)
        userLiveData.postValue(userData)
    }

    fun recalculate(user: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            nutritionInteractor.calculate(user)
        }
    }
}