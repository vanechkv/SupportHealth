package com.example.supporthealth.profile.main.domain.impl

import com.example.supporthealth.profile.main.domain.api.SettingsInteractor
import com.example.supporthealth.profile.main.domain.api.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun isDarkTheme(): Boolean {
        return repository.isDarkTheme()
    }

    override fun setDarkTheme(enabled: Boolean) {
        repository.setDarkTheme(enabled)
    }
}