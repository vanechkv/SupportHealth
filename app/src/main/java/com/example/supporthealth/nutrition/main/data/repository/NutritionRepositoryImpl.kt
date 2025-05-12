package com.example.supporthealth.nutrition.main.data.repository

import com.example.supporthealth.main.domain.api.NutritionDao
import com.example.supporthealth.main.domain.models.DailyNutritionEntity
import com.example.supporthealth.main.domain.models.DailyNutritionWithMeals
import com.example.supporthealth.main.domain.models.DailyWaterEntity
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.MealWithProducts
import com.example.supporthealth.nutrition.main.domain.api.repository.NutritionRepository
import com.example.supporthealth.nutrition.main.domain.models.DailyNorm
import com.example.supporthealth.nutrition.main.domain.models.MealState
import com.example.supporthealth.nutrition.main.domain.models.MealNutritionSummary
import com.example.supporthealth.nutrition.main.domain.models.Quadruple
import com.example.supporthealth.profile.details.domain.models.ActivityLevel
import com.example.supporthealth.profile.details.domain.models.Gender
import com.example.supporthealth.profile.details.domain.models.GoalType
import com.example.supporthealth.profile.details.domain.models.UserDetails
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale



class NutritionRepositoryImpl(
    private val nutritionDao: NutritionDao
) : NutritionRepository {

    override fun calculateDailyNorm(details: UserDetails): DailyNorm {
        val age = details.birthday.toAge()

        val bmr = when (details.gender) {
            Gender.MALE -> 10 * details.weight + 6.25 * details.height - 5 * age + 5
            Gender.FEMALE -> 10 * details.weight + 6.25 * details.height - 5 * age - 161
        }

        val activityFactor = when (details.mobility) {
            ActivityLevel.LOW -> 1.2
            ActivityLevel.MEDIUM -> 1.375
            ActivityLevel.HIGH -> 1.55
        }

        var calories = bmr * activityFactor
        calories = when (details.target) {
            GoalType.LOSE -> calories * 0.85
            GoalType.GAIN -> calories * 1.15
            GoalType.MAINTAIN -> calories
        }

        val weight = details.weight
        val proteins = weight * 2f
        val fats = weight * 1f
        val proteinCals = proteins * 4
        val fatCals = fats * 9
        val carbCals = calories - (proteinCals + fatCals)
        val carbs = carbCals / 4

        return DailyNorm(
            calories = calories.toInt(),
            proteins = "%.1f".format(Locale.US, proteins).toFloat().toInt(),
            fats = "%.1f".format(Locale.US, fats).toFloat().toInt(),
            carbs = "%.1f".format(Locale.US, carbs).toFloat().toInt()
        )
    }

    override fun calculateMealStates(
        dailyNorm: DailyNorm,
        meals: List<MealWithProducts>
    ): List<MealState> {
        val mealTypeToMeal = meals.associateBy { it.meal.mealType }

        return MealType.values().map { type ->
            val ratio = type.ratio  // 0.3, 0.4 и т.д.

            val recommendedCalories = (dailyNorm.calories * ratio).toInt()
            val recommendedProteins = (dailyNorm.proteins * ratio).toInt()
            val recommendedFats = (dailyNorm.fats * ratio).toInt()
            val recommendedCarbs = (dailyNorm.carbs * ratio).toInt()

            val consumed = mealTypeToMeal[type]?.products?.fold(
                Quadruple(0f, 0f, 0f, 0f)
            ) { acc, p ->
                Quadruple(
                    acc.first + p.calories * (p.weightGrams / 100f),
                    acc.second + p.proteins * (p.weightGrams / 100f),
                    acc.third + p.fats * (p.weightGrams / 100f),
                    acc.fourth + p.carbs * (p.weightGrams / 100f)
                )
            }

            MealState(
                mealType = type,
                recommendedCalories = recommendedCalories,
                recommendedProteins = recommendedProteins,
                recommendedFats = recommendedFats,
                recommendedCarbs = recommendedCarbs,
                consumedCalories = consumed?.first?.toInt() ?: 0,
                consumedProteins = consumed?.second?.toInt() ?: 0,
                consumedFats = consumed?.third?.toInt() ?: 0,
                consumedCarbs = consumed?.fourth?.toInt() ?: 0
            )
        }
    }

    override fun calculateRecommendedWater(userDetails: UserDetails): Int {
        return (userDetails.weight * 35)
    }

    override suspend fun upsertDailyNorm(date: String, userDetails: UserDetails) {
        val norm = calculateDailyNorm(userDetails)
        val existing = nutritionDao.getDailyNutritionByDate(date)


        val entity = DailyNutritionEntity(
            id = existing?.id ?: 0L,
            date = date,
            recommendedCalories = norm.calories,
            recommendedProteins = norm.proteins,
            recommendedFats = norm.fats,
            recommendedCarbs = norm.carbs,
            consumedCalories = existing?.consumedCalories ?: 0,
            consumedProteins = existing?.consumedProteins ?: 0,
            consumedFats = existing?.consumedFats ?: 0,
            consumedCarbs = existing?.consumedCarbs ?: 0
        )

        if (existing != null) {
            nutritionDao.updateDailyNutrition(entity)
        } else {
            nutritionDao.insertDailyNutrition(entity)
        }
    }

    override suspend fun getFullDay(date: String, userDetails: UserDetails): DailyNutritionWithMeals? {
        val stored = nutritionDao.getDayWithMeals(date)

        return if (stored != null) {
            stored
        } else {
            val norm = calculateDailyNorm(userDetails)
            val virtualEntity = DailyNutritionEntity(
                id = 0L,
                date = date,
                recommendedCalories = norm.calories,
                recommendedProteins = norm.proteins,
                recommendedFats = norm.fats,
                recommendedCarbs = norm.carbs,
                consumedCalories = 0,
                consumedProteins = 0,
                consumedFats = 0,
                consumedCarbs = 0
            )

            DailyNutritionWithMeals(
                dailyNutrition = virtualEntity,
                meals = emptyList()
            )
        }
    }

    override suspend fun getWater(date: String): DailyWaterEntity? {
        return nutritionDao.getWaterByDate(date)
    }

    override suspend fun upsertWater(date: String, userDetails: UserDetails) {
        val existing = nutritionDao.getWaterByDate(date)

        val today = LocalDate.now()
        val parsedDate = LocalDate.parse(date)

        if (parsedDate.isBefore(today) && existing != null) {
            return
        }

        val recommended = calculateRecommendedWater(userDetails)

        val entity = DailyWaterEntity(
            id = existing?.id ?: 0L,
            date = date,
            recommendedWaterMl = recommended,
            consumedWaterMl = existing?.consumedWaterMl ?: 0
        )

        if (existing != null) {
            nutritionDao.updateWater(entity)
        } else {
            nutritionDao.insertWater(entity)
        }
    }

    override suspend fun addWater(date: String, addedMl: Int) {
        val existing = nutritionDao.getWaterByDate(date)
        if (existing != null) {
            val newConsumed = (existing.consumedWaterMl + addedMl).coerceAtLeast(0)
            val updated = existing.copy(consumedWaterMl = newConsumed)
            nutritionDao.updateWater(updated)
        } else {
            val recommended = 2000
            val newEntity = DailyWaterEntity(
                date = date,
                recommendedWaterMl = recommended,
                consumedWaterMl = addedMl
            )
            nutritionDao.insertWater(newEntity)
        }
    }

    private fun String.toAge(): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val birthDate = LocalDate.parse(this, formatter)
            val today = LocalDate.now()
            Period.between(birthDate, today).years
        } catch (e: Exception) {
            25
        }
    }
}