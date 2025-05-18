package com.example.supporthealth.chat.data.repository

import com.example.supporthealth.chat.data.NetworkClientChat
import com.example.supporthealth.chat.data.dto.ChatRequest
import com.example.supporthealth.chat.domain.api.repository.ChatRepository
import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.chat.domain.models.Resource

class ChatRepositoryImpl(
    private val networkClient: NetworkClientChat
): ChatRepository {

    override fun sendMessage(message: String): Resource<ChatMessage> {

        val response = networkClient.doRequest(ChatRequest(message))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("")
            }

            200 -> {
                when {
                    response.text != null -> {
                        Resource.Success(
                            ChatMessage.Text(
                                text = response.text!!.text,
                                isUser = false
                            )
                        )
                    }
                    response.mealSuggestion != null -> {
                        Resource.Success(
                            ChatMessage.MealSuggestion(
                                mealType = response.mealSuggestion!!.mealType,
                                date = response.mealSuggestion!!.date,
                                products = response.mealSuggestion!!.products
                            )
                        )
                    }
                    else -> Resource.Error("")
                }
            }
            else -> Resource.Error("")
        }
    }
}