package com.example.supporthealth.nutrition.main.domain.api.interactor

interface AudioVoiceInteractor {

    fun getVoice(module: Int, level: Int, success: Boolean, consumer: AudioVoiceConsumer)

    fun prepareVoice(audioUrl: String?, prepared:() -> Unit, completion:() -> Unit)

    fun startPlayer()

    interface AudioVoiceConsumer {
        fun consume(audioUrl: String?, error: String?)
    }
}