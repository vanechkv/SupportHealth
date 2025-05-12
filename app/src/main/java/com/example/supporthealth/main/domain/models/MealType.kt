package com.example.supporthealth.main.domain.models

enum class MealType(val displayName: String, val ratio: Float) {
    BREAKFAST("Завтрак", 0.30f),
    LUNCH("Обед", 0.40f),
    AFTERNOON_TEA("Полдник", 0.10f),
    DINNER("Ужин", 0.20f)
}