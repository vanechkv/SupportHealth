package com.example.supporthealth.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.supporthealth.main.domain.models.HabitEntity
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

fun scheduleGoalAlarm(context: Context, habit: HabitEntity) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val triggerAtMillis = habit.attemptStartTimeMillis + habit.target * 24 * 60 * 60 * 1000L

    val intent = Intent(context, GoalAlarmReceiver::class.java).apply {
        putExtra("habitId", habit.id)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        habit.id.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    Log.d("AlarmManager", "Alarm set for habitId=${habit.id} at $triggerAtMillis")

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        triggerAtMillis,
        pendingIntent
    )
}

fun scheduleDailyUploadWorker(context: Context) {
    val now = LocalDateTime.now()
    val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
    val delay = Duration.between(now, nextMidnight).toMillis()

    val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyUploadWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .addTag("daily_upload_worker")
        .build()

    WorkManager.getInstance(context)
        .enqueueUniquePeriodicWork(
            "daily_upload_worker",
            ExistingPeriodicWorkPolicy.REPLACE,
            dailyWorkRequest
        )
}