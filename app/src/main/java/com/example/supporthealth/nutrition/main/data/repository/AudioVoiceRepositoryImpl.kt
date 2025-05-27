package com.example.supporthealth.nutrition.main.data.repository

import android.media.MediaPlayer
import android.util.Log
import com.example.supporthealth.nutrition.main.data.NetworkClientNutrition
import com.example.supporthealth.nutrition.main.data.dto.NotificationRequest
import com.example.supporthealth.nutrition.main.data.dto.NotificationResponse
import com.example.supporthealth.nutrition.main.domain.api.repository.AudioVoiceRepository
import com.example.supporthealth.nutrition.main.domain.models.Resource

class AudioVoiceRepositoryImpl(
    private val networkClient: NetworkClientNutrition,
    private val mediaPlayer: MediaPlayer
): AudioVoiceRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
    }

    private var playerState = STATE_DEFAULT

    override fun getVoice(module: Int, level: Int, success: Boolean): Resource<String> {
        val response = networkClient.doRequest(NotificationRequest(module, level, success))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("")
            }
            200 -> {
                val suggestion = (response as NotificationResponse).audioUrl
                Resource.Success(suggestion)
            }
            else -> {
                Resource.Error("")
            }
        }
    }

    override fun prepareVoice(audioUrl: String?, prepared: () -> Unit, completion: () -> Unit) {
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            prepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            completion()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }
}