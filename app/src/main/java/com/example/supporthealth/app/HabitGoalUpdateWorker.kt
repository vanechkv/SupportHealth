package com.example.supporthealth.app

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.supporthealth.main.domain.api.HabitDao
import kotlinx.coroutines.flow.first
import org.koin.core.component.get

class HabitGoalUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), org.koin.core.component.KoinComponent {

    override suspend fun doWork(): Result {
        val habitDao: HabitDao = get()
        return try {
            val habits = habitDao.getAllHabits().first()
            val now = System.currentTimeMillis()

            habits.forEach { habit ->
                val elapsedMillis = now - habit.attemptStartTimeMillis
                val elapsedDays = elapsedMillis.toFloat() / (24 * 60 * 60 * 1000)

                if (elapsedDays >= habit.target) {
                    val newTarget = habit.target * 2
                    val updatedHabit = habit.copy(target = newTarget)
                    habitDao.update(updatedHabit)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}