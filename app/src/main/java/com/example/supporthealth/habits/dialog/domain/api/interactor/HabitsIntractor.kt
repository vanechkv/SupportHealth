package com.example.supporthealth.habits.dialog.domain.api.interactor

import com.example.supporthealth.main.domain.models.HabitEntity

interface HabitsIntractor {

    suspend fun insertHabit(habit: HabitEntity)

    suspend fun updateHabit(habit: HabitEntity)

    suspend fun deleteHabit(habit: HabitEntity)
}