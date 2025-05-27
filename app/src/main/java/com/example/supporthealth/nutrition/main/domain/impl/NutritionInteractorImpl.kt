package com.example.supporthealth.nutrition.main.domain.impl

import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.MealWithProducts
import com.example.supporthealth.main.domain.models.NutritionEntity
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.nutrition.eating.domain.models.ProductWithGrams
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.api.repository.NutritionRepository
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Result
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.profile.details.domain.models.UserDetails
import kotlinx.coroutines.flow.Flow

class NutritionInteractorImpl(private val repository: NutritionRepository) : NutritionInteractor {
    override fun calculate(userDetails: UserDetails) {
        repository.calculate(userDetails)
    }

    override suspend fun calculateResult(mealId: Long): Result {
        return repository.calculateResult(mealId)
    }

    override suspend fun recalculateAllFromToday() {
        repository.recalculateAllFromToday()
    }

    override fun getNutrition(): Nutrition {
        return repository.getNutrition()
    }

    override fun getMeals(): List<Meal> {
        return repository.getMeals()
    }

    override fun getWater(): Water {
        return repository.getWater()
    }

    override suspend fun insertNutritionData(date: String) {
        repository.insertNutritionData(date)
    }

    override suspend fun updateNutrition(date: String) {
        repository.updateNutrition(date)
    }

    override suspend fun getNutritionData(date: String): NutritionEntity? {
        return repository.getNutritionData(date)
    }

    override suspend fun getNutritionFull(nutritionId: Long): NutritionFull {
        return repository.getNutritionFull(nutritionId)
    }

    override fun getNutritionInPeriod(): Flow<List<NutritionFull>> {
        return repository.getNutritionInPeriod()
    }

    override fun getNutritionInPeriodMonth(): Flow<List<NutritionFull>> {
        return repository.getNutritionInPeriodMonth()
    }

    override fun getMealWithProduct(mealId: Long): Flow<MealWithProducts> {
        return repository.getMealWithProduct(mealId)
    }

    override fun getProductsWithGrams(mealId: Long): Flow<List<ProductWithGrams>> {
        return repository.getProductsWithGrams(mealId)
    }

    override suspend fun insertWaterData(date: String) {
        repository.insertWaterData(date)
    }

    override suspend fun updateWaterData(date: String, water: Int) {
        repository.updateWaterData(date, water)
    }

    override suspend fun insertMeal(date: String) {
        repository.insertMeal(date)
    }

    override suspend fun updateMeal(date: String, mealType: MealType) {
        repository.updateMeal(date, mealType)
    }

    override suspend fun getMealId(nutritionId: Long, mealType: MealType): Long {
        return repository.getMealId(nutritionId, mealType)
    }

    override suspend fun getMealByMealId(mealId: Long): MealEntity? {
        return repository.getMealByMealId(mealId)
    }

    override suspend fun insertProduct(product: Product): Long {
        return repository.insertProduct(product)
    }

    override suspend fun deleteProductFromMeal(mealId: Long, productId: Long) {
        repository.deleteProductFromMeal(mealId, productId)
    }

    override suspend fun getProductByProductId(productId: String): ProductEntity? {
        return repository.getProductByProductId(productId)
    }

    override suspend fun addProductToMeal(mealId: Long, productId: Long, grams: Float) {
        repository.addProductToMeal(mealId, productId, grams)
    }
}