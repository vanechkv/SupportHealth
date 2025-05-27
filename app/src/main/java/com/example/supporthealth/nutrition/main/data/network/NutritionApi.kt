package com.example.supporthealth.nutrition.main.data.network

import com.example.supporthealth.nutrition.main.data.dto.NotificationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NutritionApi {

    @GET("/praise")
    fun sendRequest(@Query("module") module: Int,@Query("level") level: Int,@Query("success") success: Boolean): Call<NotificationResponse>
}