package com.example.supporthealth.profile.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.profile.main.domain.api.SettingsInteractor
import com.example.supporthealth.profile.sharing.domain.api.interactor.SharingInteractor

class ProfileViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
    private val userDetailsInteractor: UserDetailsInteractor
) :
    ViewModel() {

    private val darkThemeLiveData = MutableLiveData<Boolean>()
    fun getDarkTheme(): LiveData<Boolean> = darkThemeLiveData

    init {
        darkThemeLiveData.postValue(settingsInteractor.isDarkTheme())
    }

    fun setDarkTheme(enabled: Boolean) {
        settingsInteractor.setDarkTheme(enabled)
        darkThemeLiveData.postValue(enabled)
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun clearUserDetails( ){
        userDetailsInteractor.deleteUserDetails()
    }
}