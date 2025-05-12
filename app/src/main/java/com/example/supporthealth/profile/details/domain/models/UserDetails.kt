package com.example.supporthealth.profile.details.domain.models

data class UserDetails(
    val surname: String,
    val name: String,
    val patronymic: String,
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val birthday: String,
    val mobility: ActivityLevel,
    val target: GoalType
)
