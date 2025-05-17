package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "nutrition",
    indices = [Index(value = ["date"], unique = true)]
)
data class NutritionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val calories: Int = 0,
    val proteins: Float = 0f,
    val fats: Float = 0f,
    val carbs: Float = 0f,
    val recommendedCalories: Int,
    val recommendedProteins: Float,
    val recommendedFats: Float,
    val recommendedCarbs: Float
)
