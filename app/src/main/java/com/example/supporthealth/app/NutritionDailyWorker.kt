package com.example.supporthealth.app

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import org.joda.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.koin.core.component.get

class NutritionDailyWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), org.koin.core.component.KoinComponent {

    override suspend fun doWork(): Result {
        val nutritionInteractor: NutritionInteractor = get()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)
        val exists = nutritionInteractor.getNutritionData(today) != null

        if (!exists) {
            nutritionInteractor.insertNutritionData(today)
        }

        return Result.success()
    }
}
