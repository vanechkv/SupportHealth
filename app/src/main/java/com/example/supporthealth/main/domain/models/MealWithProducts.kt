package com.example.supporthealth.main.domain.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MealWithProducts(
    @Embedded val meal: MealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MealProductCrossRef::class,
            parentColumn = "id_meal",
            entityColumn = "id_product"
        )
    )
    val products: List<ProductEntity>
)
