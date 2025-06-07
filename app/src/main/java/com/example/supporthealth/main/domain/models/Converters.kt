package com.example.supporthealth.main.domain.models

import androidx.room.TypeConverter
import com.example.supporthealth.stress.dialog.domain.DayPart

class Converters {
    @TypeConverter
    fun fromMealType(value: MealType): String = value.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)

    @TypeConverter
    fun fromDatPart(value: DayPart): String = value.name

    @TypeConverter
    fun toDayPart(value: String): DayPart = DayPart.valueOf(value)
}