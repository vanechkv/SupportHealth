package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_water")
data class DailyWaterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val recommendedWaterMl: Int,
    val consumedWaterMl: Int
)
