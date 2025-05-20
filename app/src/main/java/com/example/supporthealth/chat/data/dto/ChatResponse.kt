package com.example.supporthealth.chat.data.dto

import com.google.gson.annotations.SerializedName

class ChatResponse (
    @SerializedName("result") val mealSuggestion: ChatMessageDto.MealSuggestion
) : Response()