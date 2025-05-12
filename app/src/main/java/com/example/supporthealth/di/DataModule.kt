package com.example.supporthealth.di

import android.content.Context
import androidx.room.Room
import com.example.supporthealth.main.data.AppDatabase
import com.example.supporthealth.nutrition.search.data.NetworkClient
import com.example.supporthealth.nutrition.search.data.network.RetrofitNetworkClient
import com.example.supporthealth.nutrition.search.data.network.SupportHealthApi
import com.example.supporthealth.profile.main.data.storage.SettingHistoryStorage
import com.example.supporthealth.welcome.detailsOnbording.data.storage.DetailsOnBordingStorage
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

private const val SUPPORT_HEALTH_PREFERENCES = "support_health_preferences"

val dataModule = module {

    single<SupportHealthApi> {
        Retrofit.Builder()
            .baseUrl("https://danilvanyamisha.loca.lt/docs/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(SupportHealthApi::class.java)
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val token = "91.214.221.99"
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    factory {
        Gson()
    }

    single {
        androidContext().getSharedPreferences(SUPPORT_HEALTH_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "nutrition-db")
            .build()
    }

    single {
        get<AppDatabase>().nutritionDao()
    }

    single {
        DetailsOnBordingStorage(get())
    }

    single {
        SettingHistoryStorage(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }
}