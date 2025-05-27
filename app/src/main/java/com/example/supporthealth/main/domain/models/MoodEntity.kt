package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "mood",
    indices = [Index(value = ["date", "dayPart"], unique = true)]
)
data class MoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,            // формат: "2025-05-26"
    val dayPart: String,         // утро, день, вечер, ночь
    val moodLevel: Int,          // от 0 до 6
    val energyLevel: Int
)