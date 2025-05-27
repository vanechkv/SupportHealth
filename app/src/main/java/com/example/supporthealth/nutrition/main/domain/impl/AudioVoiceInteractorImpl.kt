package com.example.supporthealth.nutrition.main.domain.impl

import com.example.supporthealth.nutrition.main.domain.api.interactor.AudioVoiceInteractor
import com.example.supporthealth.nutrition.main.domain.api.repository.AudioVoiceRepository
import com.example.supporthealth.nutrition.main.domain.models.Resource
import java.util.concurrent.Executors

class AudioVoiceInteractorImpl(
    private val repository: AudioVoiceRepository
) : AudioVoiceInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun getVoice(
        module: Int,
        level: Int,
        success: Boolean,
        consumer: AudioVoiceInteractor.AudioVoiceConsumer
    ) {
        executor.execute {
            when (val resource = repository.getVoice(module, level, success)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun prepareVoice(audioUrl: String?, prepared: () -> Unit, completion: () -> Unit) {
        repository.prepareVoice(audioUrl, prepared, completion)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }
}