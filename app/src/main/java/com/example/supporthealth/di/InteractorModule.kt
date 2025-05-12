package com.example.supporthealth.di

import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.example.supporthealth.profile.details.domain.impl.UserDetailsInteractorImpl
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.impl.NutritionInteractorImpl
import com.example.supporthealth.nutrition.search.domain.api.interactor.ProductInteractor
import com.example.supporthealth.nutrition.search.domain.impl.ProductInteractorImpl
import com.example.supporthealth.profile.main.domain.api.SettingsInteractor
import com.example.supporthealth.profile.main.domain.impl.SettingsInteractorImpl
import com.example.supporthealth.profile.sharing.domain.api.interactor.SharingInteractor
import com.example.supporthealth.profile.sharing.domain.impl.SharingInteractorImpl
import com.example.supporthealth.welcome.detailsOnbording.domain.api.interactor.DetailsOnBordingInteractor
import com.example.supporthealth.welcome.detailsOnbording.domain.impl.DetailsOnBordingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    single<UserDetailsInteractor> {
        UserDetailsInteractorImpl(get())
    }

    single<NutritionInteractor> {
        NutritionInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(androidContext(), get())
    }

    single<DetailsOnBordingInteractor> {
        DetailsOnBordingInteractorImpl(get())
    }

    single<ProductInteractor> {
        ProductInteractorImpl(get())
    }
}