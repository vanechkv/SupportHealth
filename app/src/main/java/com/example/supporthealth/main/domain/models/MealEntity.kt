package com.example.supporthealth.main.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal",
    foreignKeys = [ForeignKey(
        entity = NutritionEntity::class,
        parentColumns = ["id"],
        childColumns = ["id_nutrition"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mealType: MealType,
    val calories: Int = 0,
    val proteins: Float = 0f,
    val fats: Float = 0f,
    val carbs: Float = 0f,
    val recommendedCalories: Int,
    val recommendedProteins: Float,
    val recommendedFats: Float,
    val recommendedCarbs: Float,
    @ColumnInfo(name = "id_nutrition") val nutritionId: Long
)
