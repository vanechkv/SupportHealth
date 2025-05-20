package com.example.supporthealth.chat.domain.api.repository

import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.chat.domain.models.Resource

interface ChatRepository {

    fun sendMessage(message: String) : Resource<ChatMessage.MealSuggestion>
}