package com.example.supporthealth.chat.domain.models

interface ChatState {

    object Loading : ChatState

    data class Message(
        val text: ChatMessage.Text
    ) : ChatState

    data class MealSuggestion(
        val mealSuggestion: ChatMessage.MealSuggestion
    ) : ChatState

    data class Error(
        val errorMessage: Int
    ) : ChatState

    data class Empty(
        val message: Int
    ) : ChatState
}