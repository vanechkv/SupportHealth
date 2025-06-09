package com.example.supporthealth.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class GoalAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra("habitId", -1L)
        if (habitId == -1L) return

        Log.d("GoalAlarmReceiver", "Alarm received for habitId=$habitId")
        val workRequest = OneTimeWorkRequestBuilder<HabitGoalUpdateWorker>()
            .setInputData(workDataOf("habitId" to habitId))
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}