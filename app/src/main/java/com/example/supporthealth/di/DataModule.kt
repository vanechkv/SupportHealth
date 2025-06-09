package com.example.supporthealth.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.supporthealth.chat.data.NetworkClientChat
import com.example.supporthealth.chat.data.network.ChatApi
import com.example.supporthealth.chat.data.network.RetrofitNetworkClientChat
import com.example.supporthealth.main.data.AppDatabase
import com.example.supporthealth.nutrition.main.data.NetworkClientNutrition
import com.example.supporthealth.nutrition.main.data.network.NutritionApi
import com.example.supporthealth.nutrition.main.data.network.RetrofitNetworkClientNutrition
import com.example.supporthealth.nutrition.main.data.storage.NutritionStorage
import com.example.supporthealth.app.AudioVoiceWorker
import com.example.supporthealth.main.data.ApiService
import com.example.supporthealth.nutrition.search.data.NetworkClient
import com.example.supporthealth.nutrition.search.data.network.RetrofitNetworkClient
import com.example.supporthealth.nutrition.search.data.network.SupportHealthApi
import com.example.supporthealth.nutrition.search.data.storage.ProductHistoryStorage
import com.example.supporthealth.profile.details.data.storage.UserStorage
import com.example.supporthealth.profile.main.data.storage.SettingHistoryStorage
import com.example.supporthealth.welcome.detailsOnbording.data.storage.DetailsOnBordingStorage
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SUPPORT_HEALTH_PREFERENCES = "support_health_preferences"

val dataModule = module {

    single<SupportHealthApi> {
        Retrofit.Builder()
            .baseUrl("https://health-app.ru/docs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupportHealthApi::class.java)
    }

    single<ChatApi> {
        Retrofit.Builder()
            .baseUrl("https://health-app.ru/docs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApi::class.java)
    }

    single<NutritionApi> {
        Retrofit.Builder()
            .baseUrl("https://health-app.ru/docs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NutritionApi::class.java)
    }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("https://health-app.ru/docs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    factory {
        Gson()
    }

    single {
        MediaPlayer()
    }

    single {
        androidContext().getSharedPreferences(SUPPORT_HEALTH_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "support_health_db")
            .build()
    }

    single {
        get<AppDatabase>().nutritionDao()
    }

    single {
        get<AppDatabase>().mealDao()
    }

    single {
        get<AppDatabase>().productDao()
    }

    single {
        get<AppDatabase>().waterDao()
    }

    single {
        get<AppDatabase>().mealProductDao()
    }

    single {
        get<AppDatabase>().stepDao()
    }

    single {
        get<AppDatabase>().moodDao()
    }

    single {
        get<AppDatabase>().habitDao()
    }

    single {
        DetailsOnBordingStorage(get())
    }

    single {
        ProductHistoryStorage(get(), get())
    }

    single {
        NutritionStorage(get(), get())
    }

    single {
        UserStorage(get(), get())
    }

    single {
        SettingHistoryStorage(get())
    }

    single<NetworkClientChat> {
        RetrofitNetworkClientChat(androidContext(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<NetworkClientNutrition> {
        RetrofitNetworkClientNutrition(androidContext(), get())
    }

    single { WorkManager.getInstance(androidContext()) }

    factory { (context: Context, params: WorkerParameters) ->
        AudioVoiceWorker(context, params)
    }
}
