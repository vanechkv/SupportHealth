package com.example.supporthealth.main.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.supporthealth.main.domain.api.HabitDao
import com.example.supporthealth.main.domain.api.MealDao
import com.example.supporthealth.main.domain.api.MealProductDao
import com.example.supporthealth.main.domain.api.MoodDao
import com.example.supporthealth.main.domain.api.NutritionDao
import com.example.supporthealth.main.domain.api.ProductDao
import com.example.supporthealth.main.domain.api.StepDao
import com.example.supporthealth.main.domain.api.WaterDao
import com.example.supporthealth.main.domain.models.Converters
import com.example.supporthealth.main.domain.models.HabitEntity
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.NutritionEntity
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.main.domain.models.MealProductCrossRef
import com.example.supporthealth.main.domain.models.MoodEntity
import com.example.supporthealth.main.domain.models.StepEntity
import com.example.supporthealth.main.domain.models.WaterEntity


@Database(
    entities = [
        NutritionEntity::class,
        MealEntity::class,
        ProductEntity::class,
        WaterEntity::class,
        MealProductCrossRef::class,
        StepEntity::class,
        MoodEntity::class,
        HabitEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nutritionDao(): NutritionDao

    abstract fun mealDao(): MealDao

    abstract fun productDao(): ProductDao

    abstract fun waterDao(): WaterDao

    abstract fun mealProductDao(): MealProductDao

    abstract fun stepDao(): StepDao

    abstract fun moodDao(): MoodDao

    abstract fun habitDao(): HabitDao
}