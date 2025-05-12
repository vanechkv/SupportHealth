package com.example.supporthealth.nutrition.search.domain.models

data class Product(
    val productId: Int,
    val name: String?,
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int
)
