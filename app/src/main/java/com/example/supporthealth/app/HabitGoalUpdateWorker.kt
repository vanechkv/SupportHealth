package com.example.supporthealth.app

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.supporthealth.main.domain.api.HabitDao
import kotlinx.coroutines.flow.first
import org.koin.core.component.get
import java.util.concurrent.TimeUnit

import android.util.Log

class HabitGoalUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), org.koin.core.component.KoinComponent {

    override suspend fun doWork(): Result {
        val habitDao: HabitDao = get()
        return try {
            val habitId = inputData.getLong("habitId", -1L)
            if (habitId == -1L) return Result.failure()

            val habit = habitDao.getById(habitId).first()

            val now = System.currentTimeMillis()

            val elapsedMillis = now - habit.attemptStartTimeMillis
            val elapsedDays = elapsedMillis.toFloat() / (24 * 60 * 60 * 1000)
            Log.d("HabitGoalUpdateWorker", "Habit id=${habit.id}, elapsedDays=$elapsedDays, target=${habit.target}")

            if (elapsedDays >= habit.target) {
                val newTarget = habit.target * 2
                val updatedHabit = habit.copy(target = newTarget)
                habitDao.update(updatedHabit)
                Log.d("HabitGoalUpdateWorker", "Updated habit id=${habit.id}, new target=$newTarget")
            }

            Log.d("HabitGoalUpdateWorker", "Worker finished successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("HabitGoalUpdateWorker", "Error in worker", e)
            Result.failure()
        }
    }

}