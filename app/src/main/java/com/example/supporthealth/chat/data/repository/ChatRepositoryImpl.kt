package com.example.supporthealth.chat.data.repository

import com.example.supporthealth.chat.data.NetworkClientChat
import com.example.supporthealth.chat.data.dto.ChatMessageDto
import com.example.supporthealth.chat.data.dto.ChatRequest
import com.example.supporthealth.chat.data.dto.ChatResponse
import com.example.supporthealth.chat.domain.api.repository.ChatRepository
import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.chat.domain.models.ProductAi
import com.example.supporthealth.chat.domain.models.Resource
import com.example.supporthealth.main.domain.models.MealType

class ChatRepositoryImpl(
    private val networkClient: NetworkClientChat
) : ChatRepository {

    override fun sendMessage(message: String): Resource<ChatMessage.MealSuggestion> {

        val response = networkClient.doRequest(ChatRequest(message))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("")
            }

            200 -> {
                val suggestion = (response as ChatResponse).mealSuggestion
                val chatMessage = ChatMessage.MealSuggestion(
                    mealType = suggestion.russianMealToMealType(suggestion.mealType),
                    date = suggestion.date,
                    products = suggestion.products.map {
                        ProductAi(
                            productId = it.productId,
                            name = it.name,
                            calories = it.calories,
                            protein = it.protein,
                            fat = it.fat,
                            carbs = it.carbs,
                            grams = it.grams
                        )
                    }
                )
                Resource.Success(chatMessage)

            }
            else -> Resource.Error("")
        }
    }
}