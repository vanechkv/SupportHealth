package com.example.supporthealth.main.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class MealWithProducts(
    @Embedded val meal: MealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val products: List<MealProductEntity>
)
