package com.example.supporthealth.nutrition.search.data.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("product_id") val productId: String,
    val name: String,
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbs: Float
)
