package com.example.supporthealth.nutrition.search.data.dto

data class ProductDto(
    val productId: Int,
    val name: String?,
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbs: Float
)
