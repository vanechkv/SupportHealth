package com.example.supporthealth.main.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "meal_product",
    primaryKeys = ["id_meal", "id_product"],
    foreignKeys = [
        ForeignKey(entity = MealEntity::class, parentColumns = ["id"], childColumns = ["id_meal"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ProductEntity::class, parentColumns = ["id"], childColumns = ["id_product"], onDelete = ForeignKey.CASCADE)
    ]
)
data class MealProductCrossRef(
    @ColumnInfo(name = "id_meal") val mealId: Long,
    @ColumnInfo(name = "id_product") val productId: Long,
    val grams: Float
)
