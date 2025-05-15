package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val name: String,
    val calories: Int,
    val proteins: Float,
    val fats: Float,
    val carbs: Float
)
