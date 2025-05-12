package com.example.supporthealth.main.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.supporthealth.main.domain.api.NutritionDao
import com.example.supporthealth.main.domain.models.Converters
import com.example.supporthealth.main.domain.models.DailyNutritionEntity
import com.example.supporthealth.main.domain.models.DailyWaterEntity
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealProductEntity


@Database(
    entities = [
        DailyNutritionEntity::class,
        MealEntity::class,
        MealProductEntity::class,
        DailyWaterEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nutritionDao(): NutritionDao
}