package com.example.supporthealth.chat.data.dto

import com.example.supporthealth.chat.domain.models.ProductAi
import com.example.supporthealth.main.domain.models.MealType

sealed class ChatMessageDto {
    data class Text(val text: String, val isUser: Boolean): ChatMessageDto()
    data class MealSuggestion(
        val date: String,
        val mealType: MealType,
        val products: List<ProductAi>
    ): ChatMessageDto()
}
