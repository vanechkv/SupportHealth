package com.example.supporthealth.main.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "water",
    foreignKeys = [ForeignKey(
        entity = NutritionEntity::class,
        parentColumns = ["id"],
        childColumns = ["id_nutrition"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["id_nutrition"], unique = true)]
)
data class WaterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val waterMl: Int = 0,
    val recommendedWaterMl: Int,
    @ColumnInfo(name = "id_nutrition") val nutritionId: Long
)
