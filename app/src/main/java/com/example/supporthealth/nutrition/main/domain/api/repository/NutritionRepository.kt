package com.example.supporthealth.nutrition.main.domain.api.repository

import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.MealWithProducts
import com.example.supporthealth.main.domain.models.NutritionEntity
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Result
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.profile.details.domain.models.UserDetails
import kotlinx.coroutines.flow.Flow

interface NutritionRepository {

    fun calculate(userDetails: UserDetails)

    suspend fun calculateResult(mealId: Long): Result

    fun getNutrition(): Nutrition

    fun getMeals(): List<Meal>

    fun getWater(): Water

    suspend fun insertNutritionData(date: String)

    suspend fun updateNutrition(date: String)

    suspend fun getNutritionData(date: String): NutritionEntity?

    suspend fun getNutritionFull(nutritionId: Long): NutritionFull

    fun getMealWithProduct(mealId: Long): Flow<MealWithProducts>

    suspend fun insertWaterData(date: String)

    suspend fun updateWaterData(date: String, water: Int)

    suspend fun insertMeal(date: String)

    suspend fun updateMeal(date: String, mealType: MealType)

    suspend fun getMealId(nutritionId: Long, mealType: MealType): Long

    suspend fun getMealByMealId(mealId: Long): MealEntity?

    suspend fun insertProduct(product: Product): Long

    suspend fun getProductByProductId(productId: String): ProductEntity?

    suspend fun addProductToMeal(mealId: Long, productId: Long, grams: Float)
}