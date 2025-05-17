package com.example.supporthealth.main.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class NutritionFull(
    @Embedded val nutrition: NutritionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_nutrition"
    )
    val meals: List<MealEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_nutrition"
    )
    val water: WaterEntity
)
