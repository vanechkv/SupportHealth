package com.example.supporthealth.chat.data.dto

import com.example.supporthealth.main.domain.models.MealType
import com.google.gson.annotations.SerializedName

sealed class ChatMessageDto {
    data class Text(val text: String, val isUser: Boolean): ChatMessageDto()
    data class MealSuggestion(
        @SerializedName("intake_type") val mealType: String?,
        @SerializedName("entries") val products: List<ProductAiDto>
    ): ChatMessageDto() {
        fun russianMealToMealType(rus: String?): MealType = when (rus?.trim()?.lowercase()) {
            "завтрак" -> MealType.BREAKFAST
            "обед" -> MealType.LUNCH
            "ужин" -> MealType.DINNER
            "полдник" -> MealType.AFTERNOON_TEA
            else -> MealType.BREAKFAST
        }
    }
}
