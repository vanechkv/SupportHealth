package com.example.supporthealth.main.data.repository

import android.util.Log
import com.example.supporthealth.main.data.ApiService
import com.example.supporthealth.main.domain.api.*
import com.example.supporthealth.main.domain.models.*
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
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
        try {
            val remoteSteps = apiService.getSteps(userId)
            remoteSteps.forEach { step -> stepDao.insert(step) }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadSteps: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadSteps", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadSteps", e)
        }
    }

    suspend fun uploadSteps() {
        try {
            val localChanges = stepDao.getAllSteps()
            if (localChanges.isNotEmpty()) {
                apiService.uploadSteps(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadSteps: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadSteps", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadSteps", e)
        }
    }

    // ---------------- Habits ----------------
    suspend fun downloadHabits(userId: String) {
        try {
            val remote = apiService.getHabits(userId)
            remote.forEach { habit -> habitDao.insert(habit) }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadHabits: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadHabits", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadHabits", e)
        }
    }

    suspend fun uploadHabits() {
        try {
            val localChanges = habitDao.getAllHabits().first()
            if (localChanges.isNotEmpty()) {
                apiService.uploadHabits(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadHabits: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadHabits", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadHabits", e)
        }
    }

    // ---------------- Meals ----------------
    suspend fun downloadMeals(userId: String) {
        try {
            val remote = apiService.getMeals(userId)
            remote.forEach { meal -> mealDao.insertMeal(meal) }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadMeals: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadMeals", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadMeals", e)
        }
    }

    suspend fun uploadMeals() {
        try {
            val localChanges = mealDao.getMeals()
            if (localChanges.isNotEmpty()) {
                apiService.uploadMeals(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadMeals: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadMeals", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadMeals", e)
        }
    }

    // ---------------- MealProducts ----------------
    suspend fun downloadMealProducts(userId: String) {
        try {
            val remote = apiService.getMealProducts(userId)
            remote.forEach { mealProductCrossRef ->
                mealProductDao.insertMealProductCrossRef(
                    mealProductCrossRef
                )
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadMealProducts: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadMealProducts", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadMealProducts", e)
        }
    }

    suspend fun uploadMealProducts() {
        try {
            val localChanges = mealProductDao.getCrossRefs()
            if (localChanges.isNotEmpty()) {
                apiService.uploadMealProducts(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadMealProducts: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadMealProducts", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadMealProducts", e)
        }
    }

    // ---------------- Moods ----------------
    suspend fun downloadMoods(userId: String) {
        try {
            val remote = apiService.getMoods(userId)
            remote.forEach { mood -> moodDao.insertMood(mood) }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadMoods: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadMoods", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadMoods", e)
        }
    }

    suspend fun uploadMoods() {
        try {
            val localChanges = moodDao.getAllMoods().first()
            if (localChanges.isNotEmpty()) {
                apiService.uploadMoods(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadMoods: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadMoods", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadMoods", e)
        }
    }

    // ---------------- Nutrition ----------------
    suspend fun downloadNutrition(userId: String) {
        try {
            val remote = apiService.getNutrition(userId)
            remote.forEach { nutrition -> nutritionDao.insertNutrition(nutrition) }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadNutrition: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadNutrition", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadNutrition", e)
        }
    }

    suspend fun uploadNutrition() {
        try {
            val localChanges = nutritionDao.getNutrition()
            if (localChanges.isNotEmpty()) {
                apiService.uploadNutrition(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadNutrition: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadNutrition", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadNutrition", e)
        }
    }

    // ---------------- Products ----------------
    suspend fun downloadProducts(userId: String) {
        try {
            val remote = apiService.getProducts(userId)
            remote.forEach { product -> productDao.insertProduct(product) }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadProducts: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadProducts", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadProducts", e)
        }
    }

    suspend fun uploadProducts() {
        try {
            val localChanges = productDao.getAllProducts()
            if (localChanges.isNotEmpty()) {
                apiService.uploadProducts(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadProducts: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadProducts", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadProducts", e)
        }
    }

    // ---------------- Water ----------------
    suspend fun downloadWater(userId: String) {
        try {
            val remote = apiService.getWater(userId)
            remote.forEach { water -> waterDao.insertWater(water) }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadWater: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadWater", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadWater", e)
        }
    }

    suspend fun uploadWater() {
        try {
            val localChanges = waterDao.getWater()
            if (localChanges.isNotEmpty()) {
                apiService.uploadWater(userId!!, localChanges)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadWater: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadWater", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadWater", e)
        }
    }

    suspend fun downloadUser(userId: String) {
        try {
            val remote = apiService.getUser(userId)
            userDetailsInteractor.saveUserDetails(remote)
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error downloadUser: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error downloadUser", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error downloadUser", e)
        }
    }

    suspend fun uploadUser() {
        try {
            if (user != null) {
                apiService.uploadUser(userId!!, user)
            }
        } catch (e: HttpException) {
            Log.e("SyncRepository", "HTTP error uploadUser: ${e.code()} ${e.message()}")
        } catch (e: IOException) {
            Log.e("SyncRepository", "Network error uploadUser", e)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Unexpected error uploadUser", e)
        }
    }
}

