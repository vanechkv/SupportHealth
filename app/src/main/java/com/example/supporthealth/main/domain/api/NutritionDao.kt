package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.supporthealth.main.domain.models.NutritionEntity
import com.example.supporthealth.main.domain.models.NutritionFull

@Dao
interface NutritionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrition(nutrition: NutritionEntity): Long

    @Update
    suspend fun updateNutrition(nutrition: NutritionEntity)

    @Delete
    suspend fun deleteNutrition(nutrition: NutritionEntity)

    @Query("SELECT * FROM `nutrition` WHERE id = :id")
    suspend fun getNutritionById(id: Long): NutritionEntity?

    @Query("SELECT * FROM `nutrition` WHERE date = :date LIMIT 1")
    suspend fun getNutritionByDate(date: String): NutritionEntity?

    @Transaction
    @Query("SELECT * FROM `nutrition` WHERE id = :id")
    suspend fun getNutritionFull(id: Long): NutritionFull

    @Query("SELECT * FROM nutrition WHERE date >= :startDate")
    suspend fun getNutritionFromDate(startDate: String): List<NutritionEntity>
}