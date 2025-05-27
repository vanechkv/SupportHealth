package com.example.supporthealth.app

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

fun scheduleNutritionWorker(context: Context) {
    val now = LocalDateTime.now()
    val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
    val delay = Duration.between(now, nextMidnight).toMillis()

    val workRequest = OneTimeWorkRequestBuilder<NutritionDailyWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .addTag("nutrition_midnight")
        .build()

    WorkManager.getInstance(context)
        .enqueueUniqueWork("nutrition_midnight", ExistingWorkPolicy.REPLACE, workRequest)
}