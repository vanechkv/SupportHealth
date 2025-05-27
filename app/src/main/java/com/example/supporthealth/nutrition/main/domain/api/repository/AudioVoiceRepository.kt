package com.example.supporthealth.nutrition.main.domain.api.repository

import com.example.supporthealth.nutrition.main.domain.models.Resource

interface AudioVoiceRepository {

    fun getVoice(module: Int, level: Int, success: Boolean): Resource<String>

    fun prepareVoice(audioUrl: String?, prepared:() -> Unit, completion:() -> Unit)

    fun startPlayer()

    fun releasePlayer()
}