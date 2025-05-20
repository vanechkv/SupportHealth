package com.example.supporthealth.chat.domain.models

import com.example.supporthealth.main.domain.models.MealType

sealed class ChatMessage {
    data class Text(val text: String, val isUser: Boolean): ChatMessage()
    data class MealSuggestion(
        val date: String?,
        val mealType: MealType,
        val products: List<ProductAi>
    ): ChatMessage()

    object Loading : ChatMessage()
}