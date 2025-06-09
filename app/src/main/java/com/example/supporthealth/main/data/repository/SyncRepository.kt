package com.example.supporthealth.main.data.repository

import com.example.supporthealth.main.data.ApiService
import com.example.supporthealth.main.domain.api.*
import com.example.supporthealth.main.domain.models.*
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SyncRepository(
    private val apiService: ApiService,
    private val stepDao: StepDao,
    private val habitDao: HabitDao,
    private val mealDao: MealDao,
    private val mealProductDao: MealProductDao,
    private val moodDao: MoodDao,
    private val nutritionDao: NutritionDao,
    private val productDao: ProductDao,
    private val waterDao: WaterDao,
    private val userDetailsInteractor: UserDetailsInteractor
) {

    private val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val user = userDetailsInteractor.getUserDetails()


    // ---------------- Steps ----------------
    suspend fun downloadSteps(userId: String) {
        val remoteSteps = apiService.getSteps(userId)
        remoteSteps.forEach { step ->
            stepDao.insert(step)
        }
    }

    suspend fun uploadSteps() {
        val localChanges = stepDao.getAllSteps()
        if (localChanges.isNotEmpty()) {
            apiService.uploadSteps(userId!!, localChanges)
        }
    }

    // ---------------- Habits ----------------
    suspend fun downloadHabits(userId: String) {
        val remote = apiService.getHabits(userId)
        remote.forEach { habit ->
            habitDao.insert(habit)
        }
    }

    suspend fun uploadHabits() {
        val localChanges = habitDao.getAllHabits().first()
        if (localChanges.isNotEmpty()) {
            apiService.uploadHabits(userId!!, localChanges)
        }
    }

    // ---------------- Meals ----------------
    suspend fun downloadMeals(userId: String) {
        val remote = apiService.getMeals(userId)
        remote.forEach { meal ->
            mealDao.insertMeal(meal)
        }
    }

    suspend fun uploadMeals() {
        val localChanges = mealDao.getMeals()
        if (localChanges.isNotEmpty()) {
            apiService.uploadMeals(userId!!, localChanges)
        }
    }

    // ---------------- MealProducts ----------------
    suspend fun downloadMealProducts(userId: String) {
        val remote = apiService.getMealProducts(userId)
        remote.forEach { mealProductCrossRef ->
            mealProductDao.insertMealProductCrossRef(mealProductCrossRef)
        }
    }

    suspend fun uploadMealProducts() {
        val localChanges = mealProductDao.getCrossRefs()
        if (localChanges.isNotEmpty()) {
            apiService.uploadMealProducts(userId!!, localChanges)
        }
    }

    // ---------------- Moods ----------------
    suspend fun downloadMoods(userId: String) {
        val remote = apiService.getMoods(userId)
        remote.forEach { mood ->
            moodDao.insertMood(mood)
        }
    }

    suspend fun uploadMoods() {
        val localChanges = moodDao.getAllMoods().first()
        if (localChanges.isNotEmpty()) {
            apiService.uploadMoods(userId!!, localChanges)
        }
    }

    // ---------------- Nutrition ----------------
    suspend fun downloadNutrition(userId: String) {
        val remote = apiService.getNutrition(userId)
        remote.forEach { nutrition ->
            nutritionDao.insertNutrition(nutrition)
        }
    }

    suspend fun uploadNutrition() {
        val localChanges = nutritionDao.getNutrition()
        if (localChanges.isNotEmpty()) {
            apiService.uploadNutrition(userId!!, localChanges)
        }
    }

    // ---------------- Products ----------------
    suspend fun downloadProducts(userId: String) {
        val remote = apiService.getProducts(userId)
        remote.forEach { product ->
            productDao.insertProduct(product)
        }
    }

    suspend fun uploadProducts() {
        val localChanges = productDao.getAllProducts()
        if (localChanges.isNotEmpty()) {
            apiService.uploadProducts(userId!!, localChanges)
        }
    }

    // ---------------- Water ----------------
    suspend fun downloadWater(userId: String) {
        val remote = apiService.getWater(userId)
        remote.forEach { water ->
            waterDao.insertWater(water)
        }
    }

    suspend fun uploadWater() {
        val localChanges = waterDao.getWater()
        if (localChanges.isNotEmpty()) {
            apiService.uploadWater(userId!!, localChanges)
        }
    }

    suspend fun downloadUser(userId: String) {
        val remote = apiService.getUser(userId)
        userDetailsInteractor.saveUserDetails(remote)
    }

    suspend fun uploadUser() {
        if (user != null) {
            apiService.uploadUser(userId!!, user)
        }
    }
}

