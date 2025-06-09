package com.example.supporthealth.main.data

import com.example.supporthealth.main.domain.models.*
import com.example.supporthealth.profile.details.domain.models.UserDetails
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Steps
    @GET("steps")
    suspend fun getSteps(@Query("userId") userId: String): List<StepEntity>
    @POST("steps")
    suspend fun uploadSteps(@Query("userId") userId: String, @Body steps: List<StepEntity>): Response<Unit>

    // Habits
    @GET("habits")
    suspend fun getHabits(@Query("userId") userId: String): List<HabitEntity>
    @POST("habits")
    suspend fun uploadHabits(@Query("userId") userId: String, @Body habits: List<HabitEntity>): Response<Unit>

    // Meals
    @GET("meals")
    suspend fun getMeals(@Query("userId") userId: String): List<MealEntity>
    @POST("meals")
    suspend fun uploadMeals(@Query("userId") userId: String, @Body meals: List<MealEntity>): Response<Unit>

    // MealProductCrossRefs
    @GET("mealProducts")
    suspend fun getMealProducts(@Query("userId") userId: String): List<MealProductCrossRef>
    @POST("mealProducts")
    suspend fun uploadMealProducts(@Query("userId") userId: String, @Body mealProducts: List<MealProductCrossRef>): Response<Unit>

    // Moods
    @GET("moods")
    suspend fun getMoods(@Query("userId") userId: String): List<MoodEntity>
    @POST("moods")
    suspend fun uploadMoods(@Query("userId") userId: String, @Body moods: List<MoodEntity>): Response<Unit>

    // Nutrition
    @GET("nutrition")
    suspend fun getNutrition(@Query("userId") userId: String): List<NutritionEntity>
    @POST("nutrition")
    suspend fun uploadNutrition(@Query("userId") userId: String, @Body nutrition: List<NutritionEntity>): Response<Unit>

    // Products
    @GET("products")
    suspend fun getProducts(@Query("userId") userId: String): List<ProductEntity>
    @POST("products")
    suspend fun uploadProducts(@Query("userId") userId: String, @Body products: List<ProductEntity>): Response<Unit>

    // Water
    @GET("water")
    suspend fun getWater(@Query("userId") userId: String): List<WaterEntity>
    @POST("water")
    suspend fun uploadWater(@Query("userId") userId: String, @Body water: List<WaterEntity>): Response<Unit>

    // User
    @GET("user")
    suspend fun getUser(@Query("userId") userId: String): UserDetails
    @POST("user")
    suspend fun uploadUser(@Query("userId") userId: String, @Body user: UserDetails): Response<Unit>
}

