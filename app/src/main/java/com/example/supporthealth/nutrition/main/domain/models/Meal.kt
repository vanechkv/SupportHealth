package com.example.supporthealth.nutrition.main.domain.models

import android.os.Parcelable
import com.example.supporthealth.main.domain.models.MealType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meal(
    val mealType: MealType,
    val calories: Int,
    val proteins: Float,
    val fats: Float,
    val carbs: Float,
) : Parcelable
