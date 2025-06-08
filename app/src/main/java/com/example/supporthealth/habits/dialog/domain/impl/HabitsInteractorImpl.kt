package com.example.supporthealth.habits.dialog.domain.impl

import com.example.supporthealth.habits.dialog.domain.api.interactor.HabitsIntractor
import com.example.supporthealth.habits.dialog.domain.api.repository.HabitsRepository
import com.example.supporthealth.main.domain.models.HabitEntity

class HabitsInteractorImpl(
    private val repository: HabitsRepository
) : HabitsIntractor {

    override suspend fun insertHabit(habit: HabitEntity) {
        repository.insertHabit(habit)
    }

    override suspend fun updateHabit(habit: HabitEntity) {
        repository.updateHabit(habit)
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        repository.deleteHabit(habit)
    }
}