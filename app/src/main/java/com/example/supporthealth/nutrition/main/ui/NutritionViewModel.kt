package com.example.supporthealth.nutrition.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.app.AudioVoiceWorker
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.models.Level
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Module
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Result
import com.example.supporthealth.nutrition.main.domain.models.Water
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NutritionViewModel(
    private val nutritionInteractor: NutritionInteractor,
    private val workManager: WorkManager
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

    fun startAudioWorkerForYesterday() {
        viewModelScope.launch {
            val yesterday = LocalDate.now().minusDays(1)
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val yesterdayStr = yesterday.format(formatter)

            val nutritionEntity = nutritionInteractor.getNutritionData(yesterdayStr)
            if (nutritionEntity == null) {
                enqueueAudioWorker(
                    module = Module.NUTRITION.code,
                    level = Level.SOFT.code,
                    success = false
                )
                return@launch
            }

            val caloriesConsumed = nutritionEntity.calories

            val recommendedCalories = nutritionEntity.recommendedCalories

            val level = when {
                caloriesConsumed == 0 -> Level.SOFT
                caloriesConsumed < recommendedCalories * 0.8 -> Level.SOFT
                caloriesConsumed in (recommendedCalories * 0.8).toInt()..(recommendedCalories * 1.2).toInt() -> Level.BASE
                else -> Level.STRONG
            }

            val success = (level == Level.BASE)

            enqueueAudioWorker(
                module = Module.NUTRITION.code,
                level = level.code,
                success = success
            )
        }
    }

    private fun enqueueAudioWorker(module: Int, level: Int, success: Boolean) {
        val inputData = workDataOf(
            "module" to module,
            "level" to level,
            "success" to success
        )
        val request = OneTimeWorkRequestBuilder<AudioVoiceWorker>()
            .setInputData(inputData)
            .build()
        workManager.enqueueUniqueWork(
            "AudioVoiceWork",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}