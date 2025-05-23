package com.example.supporthealth.chat.data.dto

import com.google.gson.annotations.SerializedName

data class ProductAiDto(
    @SerializedName("product_id") val productId: String,
    @SerializedName("product_name") val name: String,
    @SerializedName("calories") val calories: Int,
    @SerializedName("proteins") val protein: Float,
    @SerializedName("fats") val fat: Float,
    @SerializedName("carbohydrates") val carbs: Float,
    @SerializedName("grams") var grams: Float
)
