package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_nutrition")
data class DailyNutritionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val recommendedCalories: Int,
    val recommendedProteins: Int,
    val recommendedFats: Int,
    val recommendedCarbs: Int,
    val consumedCalories: Int,
    val consumedProteins: Int,
    val consumedFats: Int,
    val consumedCarbs: Int
)
