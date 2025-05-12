package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.supporthealth.main.domain.models.DailyNutritionEntity
import com.example.supporthealth.main.domain.models.DailyNutritionWithMeals
import com.example.supporthealth.main.domain.models.DailyWaterEntity
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealProductEntity

@Dao
interface NutritionDao {

    @Transaction
    @Query("SELECT * FROM daily_nutrition WHERE date = :date")
    suspend fun getDayWithMeals(date: String): DailyNutritionWithMeals?

    @Query("SELECT * FROM daily_nutrition WHERE date = :date")
    suspend fun getDailyNutritionByDate(date: String): DailyNutritionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyNutrition(dailyNutrition: DailyNutritionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: MealProductEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Update
    suspend fun updateDailyNutrition(dailyNutrition: DailyNutritionEntity)

    @Update
    suspend fun updateProduct(product: MealProductEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Delete
    suspend fun deleteProduct(product: MealProductEntity)

    @Delete
    suspend fun deleteDailyNutrition(dailyNutrition: DailyNutritionEntity)

    @Query("SELECT * FROM daily_water WHERE date = :date LIMIT 1")
    suspend fun getWaterByDate(date: String): DailyWaterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWater(entity: DailyWaterEntity)

    @Update
    suspend fun updateWater(entity: DailyWaterEntity)
}