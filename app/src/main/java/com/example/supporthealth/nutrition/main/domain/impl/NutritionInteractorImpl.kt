package com.example.supporthealth.nutrition.main.domain.impl

import com.example.supporthealth.main.domain.models.DailyNutritionWithMeals
import com.example.supporthealth.main.domain.models.DailyWaterEntity
import com.example.supporthealth.main.domain.models.MealWithProducts
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.api.repository.NutritionRepository
import com.example.supporthealth.nutrition.main.domain.models.DailyNorm
import com.example.supporthealth.nutrition.main.domain.models.MealState
import com.example.supporthealth.nutrition.main.domain.models.MealNutritionSummary
import com.example.supporthealth.profile.details.domain.models.UserDetails

class NutritionInteractorImpl(private val repository: NutritionRepository) : NutritionInteractor {

    override fun calculateDailyNorm(details: UserDetails): DailyNorm {
        return repository.calculateDailyNorm(details)
    }

    override fun calculateMealStates(dailyNorm: DailyNorm, meals: List<MealWithProducts>): List<MealState> {
        return repository.calculateMealStates(dailyNorm, meals)
    }

    override fun calculateRecommendedWater(userDetails: UserDetails): Int {
        return repository.calculateRecommendedWater(userDetails)
    }

    override suspend fun upsertDailyNorm(date: String, userDetails: UserDetails) {
        repository.upsertDailyNorm(date, userDetails)
    }

    override suspend fun getFullDay(date: String, userDetails: UserDetails): DailyNutritionWithMeals? {
        return repository.getFullDay(date, userDetails)
    }

    override suspend fun getWater(date: String): DailyWaterEntity? {
        return repository.getWater(date)
    }

    override suspend fun upsertWater(date: String, userDetails: UserDetails) {
        repository.upsertWater(date, userDetails)
    }

    override suspend fun addWater(date: String, addedMl: Int) {
        repository.addWater(date, addedMl)
    }
}