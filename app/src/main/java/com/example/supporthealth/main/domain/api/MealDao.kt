package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.MealWithProducts

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("SELECT * FROM `meal` WHERE id_nutrition = :nutritionId AND mealType = :mealType")
    suspend fun getMealsByNutritionId(nutritionId: Long, mealType: MealType): MealEntity

    @Query("SELECT id FROM meal WHERE id_nutrition = :nutritionId AND mealType = :mealType")
    suspend fun getMealIdByNutritionIdAndMealType(nutritionId: Long, mealType: MealType): Long

    @Query("SELECT * FROM `meal` WHERE id = :id")
    suspend fun getMealById(id: Long): MealEntity?

    @Transaction
    @Query("SELECT * FROM `meal` WHERE id = :id")
    suspend fun getMealWithProducts(id: Long): MealWithProducts
}