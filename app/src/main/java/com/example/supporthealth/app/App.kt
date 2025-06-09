package com.example.supporthealth.app

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.supporthealth.di.dataModule
import com.example.supporthealth.di.interactorModule
import com.example.supporthealth.di.repositoryModule
import com.example.supporthealth.di.viewModelModule
import com.example.supporthealth.profile.main.domain.api.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }

        val settingsInteractor: SettingsInteractor by inject()
        settingsInteractor.setDarkTheme(settingsInteractor.isDarkTheme())
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ru"))
    }
}