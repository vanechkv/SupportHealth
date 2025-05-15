package com.example.supporthealth.nutrition.main.domain.api.repository

import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.NutritionEntity
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.WaterEntity
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.profile.details.domain.models.UserDetails

interface NutritionRepository {

    fun calculate(userDetails: UserDetails)

    fun getNutrition(): Nutrition

    fun getMeals(): List<Meal>

    fun getWater(): Water

    suspend fun insertNutritionData(date: String)

    suspend fun getNutritionData(date: String): NutritionEntity?

    suspend fun getNutritionFull(nutritionId: Long): NutritionFull

    suspend fun insertWaterData(date: String)

    suspend fun updateWaterData(date: String, water: Int)

    suspend fun insertMeal(date: String)

    suspend fun updateMeal(date: String, mealType: MealType)

    suspend fun getMealId(nutritionId: Long, mealType: MealType): Long

    suspend fun insertProduct(product: Product): Long

    suspend fun addProductToMeal(mealId: Long, productId: Long, grams: Float)
}