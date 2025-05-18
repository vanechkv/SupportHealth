package com.example.supporthealth.nutrition.eating.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.domain.models.MealWithProducts
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import kotlinx.coroutines.launch

class EatingViewModel(
    private val mealId: Long,
    private val nutritionInteractor: NutritionInteractor
) : ViewModel() {

    fun observeMealWithProducts(): LiveData<MealWithProducts> {
        return nutritionInteractor.getMealWithProduct(mealId).asLiveData()
    }
}