package com.example.supporthealth.nutrition.search.data.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("productId") val productId: String,
    val name: String,
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float
)
