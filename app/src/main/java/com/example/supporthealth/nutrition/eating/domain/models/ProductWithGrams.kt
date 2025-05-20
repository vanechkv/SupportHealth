package com.example.supporthealth.nutrition.eating.domain.models

data class ProductWithGrams(
    val productId: String,
    val name: String,
    val calories: Int,
    val proteins: Float,
    val fats: Float,
    val carbs: Float,
    val grams: Int
)
