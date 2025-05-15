package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.supporthealth.main.domain.models.WaterEntity

@Dao
interface WaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWater(water: WaterEntity): Long

    @Update
    suspend fun updateWater(water: WaterEntity)

    @Delete
    suspend fun deleteWater(water: WaterEntity)

    @Query("SELECT * FROM `water` WHERE id_nutrition = :nutritionId LIMIT 1")
    suspend fun getWaterByNutritionId(nutritionId: Long): WaterEntity?

    @Transaction
    suspend fun insertOrUpdateWaterByNutritionId(nutritionId: Long, data: WaterEntity): Long {
        val existing = getWaterByNutritionId(nutritionId)
        return if (existing != null) {
            val updated = data.copy(id = existing.id, nutritionId = nutritionId)
            updateWater(updated)
            existing.id
        } else {
            insertWater(data.copy(nutritionId = nutritionId))
        }
    }
}