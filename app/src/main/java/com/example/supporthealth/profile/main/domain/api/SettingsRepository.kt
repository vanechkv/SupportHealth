package com.example.supporthealth.profile.main.domain.api

interface SettingsRepository {

    fun isDarkTheme(): Boolean

    fun setDarkTheme(enabled: Boolean)
}