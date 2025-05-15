package com.example.supporthealth.nutrition.main.data.storage

import android.content.SharedPreferences
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.google.gson.Gson

class NutritionStorage(
    private val pref: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val NUTRITION_NORM = "nutrition_norm"
        private const val WATER_NORM = "water_norm"
        private const val MEAL_NORM = "meal_norm"
    }

    fun saveNutrition(nutrition: Nutrition) {
        pref.edit()
            .putString(NUTRITION_NORM, createJsonFromNutrition(nutrition))
            .apply()
    }

    fun getNutrition(): Nutrition {
        val json = pref.getString(NUTRITION_NORM, null)
        return createNutritionFromJson(json)
    }

    fun saveWater(water: Water) {
        pref.edit()
            .putString(WATER_NORM, createJsonFromWater(water))
            .apply()
    }

    fun getWater(): Water {
        val json = pref.getString(WATER_NORM, null)
        return createWaterFromJson(json)
    }

    fun saveMeals(meals: List<Meal>) {
        pref.edit()
            .putString(MEAL_NORM, createJsonFromMealList(meals))
            .apply()
    }

    fun getMeals(): List<Meal> {
        val json = pref.getString(MEAL_NORM, null)
        return createMealListFromJson(json)
    }

    private fun createJsonFromNutrition(nutrition: Nutrition): String {
        return gson.toJson(nutrition)
    }

    private fun createJsonFromWater(water: Water): String {
        return gson.toJson(water)
    }

    private fun createJsonFromMealList(meals: List<Meal>): String {
        return gson.toJson(meals)
    }

    private fun createNutritionFromJson(json: String?): Nutrition {
        return gson.fromJson(json, Nutrition::class.java)
    }

    private fun createWaterFromJson(json: String?): Water {
        return gson.fromJson(json, Water::class.java)
    }

    private fun createMealListFromJson(json: String?): List<Meal> {
        return if (json.isNullOrEmpty()) {
            ArrayList()
        } else {
            val meals = gson.fromJson(json, Array<Meal>::class.java)
            ArrayList(meals.toList())
        }
    }
}