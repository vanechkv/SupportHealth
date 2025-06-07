package com.example.supporthealth.chat.domain.api.interactor

import com.example.supporthealth.chat.domain.models.ChatMessage

interface ChatInteractor {

    fun sendMessage(message: String, consumer: ChatConsumer)

    interface ChatConsumer {
        fun consume(mealSuggestion: ChatMessage.MealSuggestion?, error: Int?)
    }
}