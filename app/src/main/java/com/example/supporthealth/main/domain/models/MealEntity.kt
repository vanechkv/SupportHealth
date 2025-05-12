package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meals",
    foreignKeys = [
        ForeignKey(
            entity = DailyNutritionEntity::class,
            parentColumns = ["id"],
            childColumns = ["dailyNutritionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dailyNutritionId")]
)
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dailyNutritionId: Long,
    val mealType: MealType,
    val recommendedCalories: Int,
    val recommendedProteins: Int,
    val recommendedFats: Int,
    val recommendedCarbs: Int,
    val consumedCalories: Int,
    val consumedProteins: Int,
    val consumedFats: Int,
    val consumedCarbs: Int
)
