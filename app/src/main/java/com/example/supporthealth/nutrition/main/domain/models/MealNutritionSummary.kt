package com.example.supporthealth.nutrition.main.domain.models

import com.example.supporthealth.main.domain.models.MealType


data class MealNutritionSummary(
    val mealType: MealType,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)

