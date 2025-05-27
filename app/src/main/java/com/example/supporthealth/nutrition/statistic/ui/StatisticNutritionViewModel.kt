package com.example.supporthealth.nutrition.statistic.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.domain.api.NutritionDao
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.profile.details.domain.models.UserDetails
import kotlinx.coroutines.launch

class StatisticNutritionViewModel(
    private val nutritionInteractor: NutritionInteractor
) : ViewModel() {

    private val nutritionLiveData = MutableLiveData<Nutrition>()
    fun observeNutrition(): LiveData<Nutrition> = nutritionLiveData

    private val nutritionDataLiveData = MutableLiveData<NutritionFull?>()
    fun observeNutritionData(): LiveData<NutritionFull?> = nutritionDataLiveData

    fun observeNutritionDataWeek(): LiveData<List<NutritionFull>> {
        return nutritionInteractor.getNutritionInPeriod().asLiveData()
    }

    fun observeNutritionDataMonth(): LiveData<List<NutritionFull>> {
        return nutritionInteractor.getNutritionInPeriodMonth().asLiveData()
    }

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
                val water = nutritionInteractor.getWater()
                nutritionDataLiveData.postValue(null)
                nutritionLiveData.postValue(nutrition)
                waterLiveData.postValue(water)
            }
        }
    }
}