package com.example.supporthealth.app

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.supporthealth.main.data.repository.SyncRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DailyUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val syncRepository: SyncRepository by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            syncRepository.uploadSteps()
            syncRepository.uploadHabits()
            syncRepository.uploadMeals()
            syncRepository.uploadMealProducts()
            syncRepository.uploadMoods()
            syncRepository.uploadNutrition()
            syncRepository.uploadProducts()
            syncRepository.uploadWater()
            syncRepository.uploadUser()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
