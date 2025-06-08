package com.example.supporthealth.habits.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.habits.dialog.domain.api.interactor.HabitsIntractor
import com.example.supporthealth.main.domain.api.HabitDao
import com.example.supporthealth.main.domain.models.HabitEntity
import kotlinx.coroutines.launch

class HabitsViewModel(
    private val habitDao: HabitDao,
    private val habitsIntractor: HabitsIntractor
) : ViewModel() {

    fun observeHabitsData(): LiveData<List<HabitEntity>> {
        return habitDao.getAllHabits().asLiveData()
    }

    fun observeHabit(habitId: Long): LiveData<HabitEntity> {
        return habitDao.getById(habitId).asLiveData()
    }

    fun insertHabit(habit: HabitEntity) {
        viewModelScope.launch {
            habitsIntractor.insertHabit(habit)
        }
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            habitsIntractor.updateHabit(habit)
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            habitsIntractor.deleteHabit(habit)
        }
    }
}