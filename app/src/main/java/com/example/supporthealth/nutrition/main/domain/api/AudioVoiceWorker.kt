package com.example.supporthealth.nutrition.main.domain.api

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.supporthealth.nutrition.main.domain.api.interactor.AudioVoiceInteractor
import kotlinx.coroutines.CompletableDeferred
import java.time.LocalDate
import org.koin.core.component.get

class AudioVoiceWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams), org.koin.core.component.KoinComponent {

    private val audioVoiceInteractor: AudioVoiceInteractor = get()

    override suspend fun doWork(): Result {

        if (!canPlayToday(applicationContext)) {
            return Result.success()
        }

        val module = inputData.getInt("module", 0)
        val level = inputData.getInt("level", 0)
        val success = inputData.getBoolean("success", false)

        val deferredResult = CompletableDeferred<Result>()

        audioVoiceInteractor.getVoice(module, level, success, object : AudioVoiceInteractor.AudioVoiceConsumer {
            override fun consume(audioUrl: String?, error: String?) {
                if (audioUrl != null) {
                    Log.d("AudioVoiceWorker", "Audio URL получен: $audioUrl")
                    audioVoiceInteractor.prepareVoice(audioUrl, {
                        Log.d("AudioVoiceWorker", "Audio prepared, starting playback")
                        audioVoiceInteractor.startPlayer()
                    }, {
                        Log.d("AudioVoiceWorker", "Audio playback completed")
                        markPlayedToday(applicationContext)
                        deferredResult.complete(Result.success())
                    })
                } else {
                    Log.e("AudioVoiceWorker", "Ошибка получения аудио: $error")
                    deferredResult.complete(Result.success())
                }
            }
        })

        return deferredResult.await()
    }

    private fun canPlayToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences("audio_prefs", Context.MODE_PRIVATE)
        val lastDate = prefs.getString("last_played_date", null)
        val today = LocalDate.now().toString()
        return lastDate != today
    }

    private fun markPlayedToday(context: Context) {
        val prefs = context.getSharedPreferences("audio_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_played_date", LocalDate.now().toString()).apply()
    }
}