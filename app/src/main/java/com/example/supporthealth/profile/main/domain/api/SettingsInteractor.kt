package com.example.supporthealth.profile.main.domain.api

interface SettingsInteractor {

    fun isDarkTheme(): Boolean

    fun setDarkTheme(enabled: Boolean)
}