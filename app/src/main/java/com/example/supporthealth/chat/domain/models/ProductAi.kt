package com.example.supporthealth.chat.domain.models

data class ProductAi(
    val productId: String,
    val name: String,
    val calories: Int,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    var grams: Float
)
