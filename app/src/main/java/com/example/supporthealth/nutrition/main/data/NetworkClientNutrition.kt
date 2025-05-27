package com.example.supporthealth.nutrition.main.data

import com.example.supporthealth.nutrition.main.data.dto.Response

interface NetworkClientNutrition {
    fun doRequest(dto: Any): Response
}