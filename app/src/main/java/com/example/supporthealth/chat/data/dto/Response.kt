package com.example.supporthealth.chat.data.dto

open class Response {
    var resultCode = 0
    var text: ChatMessageDto.Text? = null
    var mealSuggestion: ChatMessageDto.MealSuggestion? = null
}