package com.example.supporthealth.nutrition.main.data.repository

import com.example.supporthealth.main.domain.api.MealDao
import com.example.supporthealth.main.domain.api.MealProductDao
import com.example.supporthealth.main.domain.api.NutritionDao
import com.example.supporthealth.main.domain.api.ProductDao
import com.example.supporthealth.main.domain.api.WaterDao
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.NutritionEntity
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.main.domain.models.WaterEntity
import com.example.supporthealth.nutrition.main.data.storage.NutritionStorage
import com.example.supporthealth.nutrition.main.domain.api.repository.NutritionRepository
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.profile.details.domain.models.ActivityLevel
import com.example.supporthealth.profile.details.domain.models.Gender
import com.example.supporthealth.profile.details.domain.models.GoalType
import com.example.supporthealth.profile.details.domain.models.UserDetails
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale


class NutritionRepositoryImpl(
    private val nutritionDao: NutritionDao,
    private val mealDao: MealDao,
    private val waterDao: WaterDao,
    private val productDao: ProductDao,
    private val mealProductDao: MealProductDao,
    private val nutritionStorage: NutritionStorage
) : NutritionRepository {

    override fun calculate(userDetails: UserDetails) {
        val age = userDetails.birthday.toAge()

        val bmr = when (userDetails.gender) {
            Gender.MALE -> 10 * userDetails.weight + 6.25 * userDetails.height - 5 * age + 5
            Gender.FEMALE -> 10 * userDetails.weight + 6.25 * userDetails.height - 5 * age - 161
        }

        val activityFactor = when (userDetails.mobility) {
            ActivityLevel.LOW -> 1.2
            ActivityLevel.MEDIUM -> 1.375
            ActivityLevel.HIGH -> 1.55
        }

        var calories = bmr * activityFactor
        calories = when (userDetails.target) {
            GoalType.LOSE -> calories * 0.85
            GoalType.GAIN -> calories * 1.15
            GoalType.MAINTAIN -> calories
        }

        val weight = userDetails.weight
        val proteins = weight * 2f
        val fats = weight * 1f
        val proteinCals = proteins * 4
        val fatCals = fats * 9
        val carbCals = calories - (proteinCals + fatCals)
        val carbs = carbCals / 4

        val nutrition = Nutrition(
            calories = calories.toInt(),
            proteins = "%.1f".format(Locale.US, proteins).toFloat(),
            fats = "%.1f".format(Locale.US, fats).toFloat(),
            carbs = "%.1f".format(Locale.US, carbs).toFloat(),
        )

        val meals = MealType.values().map { meal ->
            Meal(
                mealType = meal,
                calories = (nutrition.calories * meal.ratio).toInt(),
                proteins = nutrition.proteins * meal.ratio,
                fats = nutrition.fats * meal.ratio,
                carbs = nutrition.carbs * meal.ratio
            )
        }

        val water = Water(
            waterMl = userDetails.weight * 30
        )

        nutritionStorage.saveNutrition(nutrition)
        nutritionStorage.saveMeals(meals)
        nutritionStorage.saveWater(water)
    }

    override fun getNutrition(): Nutrition {
        return nutritionStorage.getNutrition()
    }

    override fun getMeals(): List<Meal> {
        return nutritionStorage.getMeals()
    }

    override fun getWater(): Water {
        return nutritionStorage.getWater()
    }

    override suspend fun insertNutritionData(date: String) {
        val nutrition = getNutrition()
        val nutritionData = NutritionEntity(
            date = date,
            recommendedCalories = nutrition.calories,
            recommendedProteins = nutrition.proteins,
            recommendedFats = nutrition.fats,
            recommendedCarbs = nutrition.carbs
        )
        nutritionDao.insertNutrition(nutritionData)
        insertMeal(date)
        insertWaterData(date)
    }

    override suspend fun getNutritionData(date: String): NutritionEntity? {
        return nutritionDao.getNutritionByDate(date)
    }

    override suspend fun getNutritionFull(nutritionId: Long): NutritionFull {
        return nutritionDao.getNutritionFull(nutritionId)
    }

    override suspend fun insertWaterData(date: String) {
        var nutrition = getNutritionData(date)
        val water = getWater()
        val waterData: WaterEntity
        if (nutrition != null) {
            waterData = WaterEntity(
                recommendedWaterMl = water.waterMl,
                nutritionId = nutrition.id
            )
        } else {
            insertNutritionData(date)
            nutrition = getNutritionData(date)
            waterData = WaterEntity(
                recommendedWaterMl = water.waterMl,
                nutritionId = nutrition!!.id
            )
        }
        waterDao.insertWater(waterData)
    }

    override suspend fun updateWaterData(date: String, water: Int) {
        var nutrition = getNutritionData(date)
        val waterRecommend = getWater()
        val nutritionId: Long

        if (nutrition == null) {
            insertNutritionData(date)
            insertWaterData(date)
            nutrition = getNutritionData(date)
        }

        nutritionId = nutrition!!.id
        val existingWater = waterDao.getWaterByNutritionId(nutritionId)

        val newWaterValue = if (existingWater != null) {
            val updatedValue = (existingWater.waterMl + water).coerceAtLeast(0)
            existingWater.copy(
                waterMl = updatedValue,
                recommendedWaterMl = waterRecommend.waterMl
            )
        } else {
            WaterEntity(
                waterMl = water.coerceAtLeast(0),
                recommendedWaterMl = waterRecommend.waterMl,
                nutritionId = nutritionId
            )
        }

        if (existingWater != null) {
            waterDao.updateWater(newWaterValue)
        } else {
            waterDao.insertWater(newWaterValue)
        }
    }

    override suspend fun insertMeal(date: String) {
        var nutrition = getNutritionData(date)
        val meals = getMeals()

        if (nutrition == null) {
            insertNutritionData(date)
            nutrition = getNutritionData(date)
        }

        val nutritionId = nutrition!!.id

        meals.forEach { meal ->
            val mealData = MealEntity(
                mealType = meal.mealType,
                recommendedCalories = meal.calories,
                recommendedProteins = meal.proteins,
                recommendedFats = meal.fats,
                recommendedCarbs = meal.carbs,
                nutritionId = nutritionId
            )
            mealDao.insertMeal(mealData)
        }
    }

    override suspend fun updateMeal(date: String, mealType: MealType) {
        var nutrition = getNutritionData(date)
        if (nutrition == null) {
            insertNutritionData(date)
            nutrition = getNutritionData(date)
        }

        val nutritionId = nutrition!!.id
        val meal = mealDao.getMealsByNutritionId(nutritionId, mealType)
        val mealId = meal.id

        val crossRefs = mealProductDao.getCrossRefsByMealId(mealId)
        val products = productDao.getProductsByMealId(mealId)

        var totalCalories = 0
        var totalProteins = 0f
        var totalFats = 0f
        var totalCarbs = 0f

        crossRefs.forEach { ref ->
            val product = products.find { it.id == ref.productId }
            if (product != null) {
                val factor = ref.grams / 100f
                totalCalories += (product.calories * factor).toInt()
                totalProteins += product.proteins * factor
                totalFats     += product.fats * factor
                totalCarbs    += product.carbs * factor
            }
        }

        val updatedMeal = meal.copy(
            calories = totalCalories,
            proteins = totalProteins,
            fats = totalFats,
            carbs = totalCarbs
        )

        mealDao.updateMeal(updatedMeal)
    }

    override suspend fun getMealId(nutritionId: Long, mealType: MealType): Long {
        return mealDao.getMealIdByNutritionIdAndMealType(nutritionId, mealType)
    }

    override suspend fun insertProduct(product: Product): Long {
        val productData = ProductEntity(
            productId = product.productId,
            name = product.name,
            calories = product.calories,
            proteins = product.protein,
            fats = product.fat,
            carbs = product.carbs
        )
        return productDao.insertProduct(productData)
    }

    override suspend fun addProductToMeal(mealId: Long, productId: Long, grams: Float) {
        mealProductDao.insertOrUpdateMealProduct(mealId, productId, grams)
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