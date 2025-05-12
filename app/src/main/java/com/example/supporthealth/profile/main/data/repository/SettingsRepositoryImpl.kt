package com.example.supporthealth.profile.main.data.repository

import com.example.supporthealth.profile.main.data.storage.SettingHistoryStorage
import com.example.supporthealth.profile.main.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val darkMode: SettingHistoryStorage) : SettingsRepository {
    override fun isDarkTheme(): Boolean {
        return darkMode.isDarkTheme()
    }

    override fun setDarkTheme(enabled: Boolean) {
        darkMode.setDarkTheme(enabled)
    }
}