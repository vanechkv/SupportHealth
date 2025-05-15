package com.example.supporthealth.nutrition.search.domain.models

data class Product(
    val productId: String,
    val name: String,
    val calories: Int,
    val protein: Float,
    val fat: Float,
    val carbs: Float
)
