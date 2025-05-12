package com.example.supporthealth.nutrition.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.domain.models.DailyNutritionWithMeals
import com.example.supporthealth.main.domain.models.DailyWaterEntity
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.models.MealState
import kotlinx.coroutines.launch

class NutritionViewModel(
    private val nutritionInteractor: NutritionInteractor,
    private val userDetailsInteractor: UserDetailsInteractor
) : ViewModel() {

    private val dailyNormLiveData = MutableLiveData<DailyNutritionWithMeals?>()
    fun observeDailyNorm(): LiveData<DailyNutritionWithMeals?> = dailyNormLiveData

    private val mealStatesLiveData = MutableLiveData<List<MealState>>()
    fun observeMealStates(): LiveData<List<MealState>> = mealStatesLiveData

    private val waterLiveData = MutableLiveData<DailyWaterEntity?>()
    fun observeWater(): LiveData<DailyWaterEntity?> = waterLiveData

    fun loadFullDay(date: String) {
        viewModelScope.launch {
            val userDetails = userDetailsInteractor.loadUserDate()
            val fullDay = nutritionInteractor.getFullDay(date, userDetails)
            dailyNormLiveData.postValue(fullDay)

            val norm = nutritionInteractor.calculateDailyNorm(userDetails)
            val states = nutritionInteractor.calculateMealStates(norm, fullDay?.meals ?: emptyList())
            mealStatesLiveData.postValue(states)
        }
    }

    fun loadWater(date: String) {
        viewModelScope.launch {
            val user = userDetailsInteractor.loadUserDate()
            nutritionInteractor.upsertWater(date, user)
            val water = nutritionInteractor.getWater(date)
            waterLiveData.postValue(water)
        }
    }

    fun addWater(date: String, amount: Int) {
        viewModelScope.launch {
            nutritionInteractor.addWater(date, amount)
            loadWater(date)
        }
    }
}