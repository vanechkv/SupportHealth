package com.example.supporthealth.main.domain.models

import androidx.room.Embedded
import androidx.room.Relation


data class DailyNutritionWithMeals(
    @Embedded val dailyNutrition: DailyNutritionEntity,
    @Relation(
        entity = MealEntity::class,
        parentColumn = "id",
        entityColumn = "dailyNutritionId"
    )
    val meals: List<MealWithProducts>
)
