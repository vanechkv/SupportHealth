package com.example.supporthealth.chat.data.dto

class ChatResponse (
    val resultCode: Int,
    val text: ChatMessageDto.Text? = null,
    val mealSuggestion: ChatMessageDto.MealSuggestion? = null
)