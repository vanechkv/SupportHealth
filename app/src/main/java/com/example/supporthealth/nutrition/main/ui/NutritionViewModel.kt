package com.example.supporthealth.nutrition.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.WaterEntity
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Water
import kotlinx.coroutines.launch

class NutritionViewModel(
    private val nutritionInteractor: NutritionInteractor
) : ViewModel() {

    private val nutritionLiveData = MutableLiveData<Nutrition>()
    fun observeNutrition(): LiveData<Nutrition> = nutritionLiveData

    private val nutritionDataLiveData = MutableLiveData<NutritionFull?>()
    fun observeNutritionData(): LiveData<NutritionFull?> = nutritionDataLiveData

    private val mealsLiveData = MutableLiveData<List<Meal>>()
    fun observeMeals(): LiveData<List<Meal>> = mealsLiveData

    private val waterLiveData = MutableLiveData<Water>()
    fun observeWater(): LiveData<Water> = waterLiveData

    fun loadDay(date: String) {
        viewModelScope.launch {
            val nutritionEntity = nutritionInteractor.getNutritionData(date)
            if (nutritionEntity != null) {
                val full = nutritionInteractor.getNutritionFull(nutritionEntity.id)
                nutritionDataLiveData.postValue(full)
            } else {
                val nutrition = nutritionInteractor.getNutrition()
                val meals = nutritionInteractor.getMeals()
                val water = nutritionInteractor.getWater()

                nutritionDataLiveData.postValue(null)
                nutritionLiveData.postValue(nutrition)
                mealsLiveData.postValue(meals)
                waterLiveData.postValue(water)
            }
        }
    }

    fun updateWater(date: String, water: Int) {
        viewModelScope.launch {
            nutritionInteractor.updateWaterData(date, water)
            loadDay(date)
        }
    }
}