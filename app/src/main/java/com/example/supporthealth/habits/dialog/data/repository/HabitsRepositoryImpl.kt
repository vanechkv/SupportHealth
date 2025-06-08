package com.example.supporthealth.habits.dialog.data.repository

import com.example.supporthealth.habits.dialog.domain.api.repository.HabitsRepository
import com.example.supporthealth.main.domain.api.HabitDao
import com.example.supporthealth.main.domain.models.HabitEntity

class HabitsRepositoryImpl(
    private val habitDao: HabitDao
) : HabitsRepository {

    override suspend fun insertHabit(habit: HabitEntity) {
        habitDao.insert(habit)
    }

    override suspend fun updateHabit(habit: HabitEntity) {
        habitDao.update(habit)
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.delete(habit)
    }
}