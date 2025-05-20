package com.example.supporthealth.chat.data.dto

import com.google.gson.annotations.SerializedName

data class ProductAiDto(
    @SerializedName("product_id") val productId: String,
    @SerializedName("product_name") val name: String,
    @SerializedName("Ккал") val calories: Int,
    @SerializedName("Белки") val protein: Float,
    @SerializedName("Жиры") val fat: Float,
    @SerializedName("Углеводы") val carbs: Float,
    @SerializedName("amount_g") var grams: Float
)
