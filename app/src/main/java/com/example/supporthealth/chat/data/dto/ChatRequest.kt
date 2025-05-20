package com.example.supporthealth.chat.data.dto

import com.google.gson.annotations.SerializedName

data class ChatRequest(@SerializedName("text") val message: String)