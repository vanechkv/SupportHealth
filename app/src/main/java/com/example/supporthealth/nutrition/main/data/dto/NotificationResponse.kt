package com.example.supporthealth.nutrition.main.data.dto

import com.google.gson.annotations.SerializedName

class NotificationResponse(@SerializedName("audio_url") val audioUrl: String): Response()
