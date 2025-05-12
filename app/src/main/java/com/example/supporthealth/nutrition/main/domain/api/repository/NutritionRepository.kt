package com.example.supporthealth.nutrition.main.domain.api.repository

import com.example.supporthealth.main.domain.models.DailyNutritionWithMeals
import com.example.supporthealth.main.domain.models.DailyWaterEntity
import com.example.supporthealth.main.domain.models.MealWithProducts
import com.example.supporthealth.nutrition.main.domain.models.DailyNorm
import com.example.supporthealth.nutrition.main.domain.models.MealState
import com.example.supporthealth.nutrition.main.domain.models.MealNutritionSummary
import com.example.supporthealth.profile.details.domain.models.UserDetails

interface NutritionRepository {

    fun calculateDailyNorm(details: UserDetails): DailyNorm
    fun calculateMealStates(dailyNorm: DailyNorm, meals: List<MealWithProducts>): List<MealState>
    fun calculateRecommendedWater(userDetails: UserDetails): Int
    suspend fun upsertDailyNorm(date: String, userDetails: UserDetails)
    suspend fun getFullDay(date: String, userDetails: UserDetails): DailyNutritionWithMeals?
    suspend fun getWater(date: String): DailyWaterEntity?
    suspend fun upsertWater(date: String, userDetails: UserDetails)
    suspend fun addWater(date: String, addedMl: Int)
}