package com.example.supporthealth.nutrition.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.WaterEntity
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Result
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

    private val mealResultLiveData = MutableLiveData<Map<MealType, Result>?>()
    fun observeMealResult(): LiveData<Map<MealType, Result>?> = mealResultLiveData

    private val mealResults = mutableMapOf<MealType, Result>()

    fun loadDay(date: String) {
        viewModelScope.launch {
            mealResults.clear()
            mealResultLiveData.postValue(emptyMap())
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

    fun calculateMealResult(mealId: Long, mealType: MealType) {
        viewModelScope.launch {
            val result = nutritionInteractor.calculateResult(mealId)
            mealResults[mealType] = result
            mealResultLiveData.postValue(mealResults.toMap())
        }
    }
}