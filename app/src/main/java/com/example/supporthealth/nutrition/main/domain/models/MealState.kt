package com.example.supporthealth.nutrition.main.domain.models

import com.example.supporthealth.main.domain.models.MealType

data class MealState(
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
