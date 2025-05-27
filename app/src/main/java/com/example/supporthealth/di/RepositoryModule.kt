package com.example.supporthealth.di

import com.example.supporthealth.activity.main.data.repository.ActivityRepositoryImpl
import com.example.supporthealth.activity.main.domain.api.repository.ActivityRepository
import com.example.supporthealth.chat.data.repository.ChatRepositoryImpl
import com.example.supporthealth.chat.domain.api.repository.ChatRepository
import com.example.supporthealth.nutrition.main.data.repository.AudioVoiceRepositoryImpl
import com.example.supporthealth.profile.details.data.repository.UserDetailsRepositoryImpl
import com.example.supporthealth.profile.details.domain.api.repository.UserDetailsRepository
import com.example.supporthealth.nutrition.main.data.repository.NutritionRepositoryImpl
import com.example.supporthealth.nutrition.main.domain.api.repository.AudioVoiceRepository
import com.example.supporthealth.nutrition.main.domain.api.repository.NutritionRepository
import com.example.supporthealth.nutrition.search.data.repository.ProductRepositoryImpl
import com.example.supporthealth.nutrition.search.domain.api.repository.ProductRepository
import com.example.supporthealth.profile.main.data.repository.SettingsRepositoryImpl
import com.example.supporthealth.profile.main.domain.api.SettingsRepository
import com.example.supporthealth.profile.sharing.domain.api.repository.ExternalNavigator
import com.example.supporthealth.profile.sharing.domain.impl.ExternalNavigatorImpl
import com.example.supporthealth.welcome.detailsOnbording.data.repository.DetailsOnBordingRepositoryImpl
import com.example.supporthealth.welcome.detailsOnbording.domain.api.repository.DetailsOnBordingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<UserDetailsRepository> {
        UserDetailsRepositoryImpl(get())
    }

    single<NutritionRepository> {
        NutritionRepositoryImpl(get(), get(), get(), get(), get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<DetailsOnBordingRepository> {
        DetailsOnBordingRepositoryImpl(get())
    }

    single<ProductRepository> {
        ProductRepositoryImpl(get(), get())
    }

    single<ChatRepository> {
        ChatRepositoryImpl(get())
    }

    single<ActivityRepository> {
        ActivityRepositoryImpl(get(), get())
    }

    single<AudioVoiceRepository> {
        AudioVoiceRepositoryImpl(get(), get())
    }
}