package com.example.supporthealth.habits.dialog.domain.api.repository

import com.example.supporthealth.main.domain.models.HabitEntity

interface HabitsRepository {

    suspend fun insertHabit(habit: HabitEntity)

    suspend fun updateHabit(habit: HabitEntity)

    suspend fun deleteHabit(habit: HabitEntity)
}